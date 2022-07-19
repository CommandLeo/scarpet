// Item Randomizer by CommandLeo

global_item_list = item_list();
global_app_name = system_info('app_name');

__config() -> {
    'commands' -> {
        '' -> 'help',
        'create <name> items <items>' -> 'createFromItems',
        'create <name> fromContainer' -> ['createFromContainer', null],
        'create <name> fromContainer <pos>' -> 'createFromContainer',
        'create <name> fromArea <from_pos> <to_pos>' -> 'createFromArea',
        'clone <table> <name>' -> 'cloneTable',
        'delete <table>' -> ['deleteTable', false],
        'delete <table> confirm' -> ['deleteTable', true],
        'edit <table>' -> 'editTable',
        'edit <table> add items <items>' -> 'addItems',
        'edit <table> add fromTable <other_table>' -> 'addFromTable',
        'edit <table> remove items <entries>' -> 'removeItems',
        'edit <table> remove fromTable <other_table>' -> 'removeFromTable',
        'list' -> 'listTables',
        'info <table>' -> 'info',
        'view <table>' -> 'view',
        'insert <mode> <table>' -> ['insert', null],
        'insert <mode> <table> <pos>' -> 'insert',
        'export <mode> <table>' -> 'exportDatapack'
    },
    'arguments' -> {
        'name' -> {
            'type' -> 'term',
            'suggest' -> [],
            'case_sensitive' -> false
        },
        'mode' -> {
            'type' -> 'term',
            'options' -> ['single', 'single_random', 'single_stack', 'random', 'random_stacks', 'shulker_full', 'shulker_random'],
            'case_sensitive' -> false
        },
        'items' -> {
            'type' -> 'text',
            'suggester' -> _(args) -> (
                i = args:'items';
                items = split(' ', i);
                if(length(items) && slice(i, -1) != ' ', delete(items, -1));
                items_string = join(' ', items);
                return(if(items, map(global_item_list, str('%s %s', items_string, _)), global_item_list));
            ),
            'case_sensitive' -> false
        },
        'table' -> {
            'type' -> 'term',
            'suggester' -> _(args) -> list_files('', 'text'),
            'case_sensitive' -> false
        },
        'other_table' -> {
            'type' -> 'term',
            'suggester' -> _(args) -> list_files('', 'text'),
            'case_sensitive' -> false
        },
        'entries' -> {
            'type' -> 'text',
            'suggester' -> _(args) -> (
                table = args:'table';
                table_items = map(_readTable(table), _:0);
                i = args:'items';
                entries = split(' ', i);
                if(entries && slice(i, -1) != ' ', delete(entries, -1));
                entries_string = join(' ', entries);
                return(if(entries, map(table_items, str('%s %s', entries_string, _)), table_items));
            ),
            'case_sensitive' -> false
        },
        'from_pos' -> {
            'type' -> 'pos'
        },
        'to_pos' -> {
            'type' -> 'pos'
        }
    },
    'requires' -> {
        'carpet' -> '>=1.4.57'
    },
    'scope' -> 'global'
};

// HELPER FUNCTIONS

_error(error) -> exit(print(format(str('r %s', error))));

_readTable(table) -> (
    regex = '(\\w+)(\\{.+\\})?';
    entries = map(read_file(table, 'text'), results = _~regex; [results:0, results:1 || null]);
    filter(entries, _ && _:0 != 'air' && global_item_list~(_:0) != null && parse_nbt(_:1) != null);
);

_getItemFromBlock(block) -> (
    if(!block, return());
    if(global_item_list~block != null, return(str(block)));

    [x, y, z] = pos(block);
    dummy_y = -1000;
    run(str('loot spawn %d %d %d mine %d %d %d shears{Enchantments:[{id:silk_touch,lvl:1}]}', x, dummy_y, z, x, y, z));
    items = entity_area('item', [x, dummy_y, z], [0.5, 0.5, 0.5]);
    item = items:(-1);
    for(items, modify(_, 'remove'));
    return(if(item, item~'item':0));
);

_setInfo(screen, page_count, pages_length, items_length) -> (
    inventory_set(screen, 49, 1, 'paper', str('{display:{Name:\'{"text":"Page %d/%d","color":"#9B59B6","italic":false}\',Lore:[\'{"text":"%s entries","color":"gray","italic":false}\']}}', page_count % pages_length + 1, pages_length, items_length));
);

_setItems(screen, page) -> (
    for(range(45), [item, nbt] = page:_i; try(inventory_set(screen, _i, if(_i < length(page), 1), item, nbt), 'unknown_item', inventory_set(screen, _i, 0)));
);

// MAIN

help() -> (
    texts = [
        'fs ' + ' ' * 80, ' \n',
        '#8E44ADb Item Randomizer ', 'g by ', '#9B59B6b CommandLeo', '^g https://github.com/CommandLeo', ' \n\n',
        '#9B59B6 /app_name create <name> items <items> ', 'f ｜ ', 'g Creates a table from a list of items', ' \n',
        '#9B59B6 /app_name create <name> fromContainer [<pos>] ', 'f ｜ ', 'g Creates a table from the items inside the container you are looking at or at the specified coords', ' \n',
        '#9B59B6 /app_name create <name> fromArea <start> <end> ', 'f ｜ ', 'g Creates a table from the blocks within the specified coordinates', ' \n',
        '#9B59B6 /app_name delete <table> ', 'f ｜ ', 'g Deletes a table', ' \n',
        '#9B59B6 /app_name clone <table> <name> ', 'f ｜ ', 'g Creates a new table with the content of another table', ' \n',
        '#9B59B6 /app_name list ', 'f ｜ ', 'g Lists all existing tables', ' \n',
        '#9B59B6 /app_name info <table> ', 'f ｜ ', 'g Displays information about a table', ' \n',
        '#9B59B6 /app_name view <table> ', 'f ｜ ', 'g Displays the content of a table inside a fancy GUI', ' \n',
        '#9B59B6 /app_name edit <table> ', 'f ｜ ', 'g Prints a menu to edit a table', ' \n',
        '#9B59B6 /app_name edit <table> add items <items> ', 'f ｜ ', 'g Adds items to a table', ' \n',
        '#9B59B6 /app_name edit <table> add fromTable <table> ', 'f ｜ ', 'g Adds items to a table from another table', ' \n',
        '#9B59B6 /app_name edit <table> remove items <items> ', 'f ｜ ', 'g Removes items from a table', ' \n',
        '#9B59B6 /app_name edit <table> remove fromTable <table> ', 'f ｜ ', 'g Remove from a table the items contained in another table', ' \n',
        '#9B59B6 /app_name insert <mode> <table> [<pos>] ', 'f ｜ ', 'g Inserts the table in the container you are looking at or at the specified coords', ' \n',
        '#9B59B6 /app_name export <mode> <table> ', 'f ｜ ', 'g Exports the table as a loot table in a datapack', ' \n',
        'fs ' + ' ' * 80
    ];
    print(format(map(texts, replace(_, 'app_name', global_app_name))));
);

createFromItems(name, items_string) -> (
    items = split(' ', items_string);
    invalid_items = {};
    for(items, if(global_item_list~_ == null, invalid_items += _));
    if(invalid_items, _error(str('Invalid items: %s', join(', ', keys(invalid_items)))));
    createTable(name, map(items, [_, null]));
);

createFromContainer(name, position) -> (
    target = if(position, block(position), player()~'trace');
    if(!target || inventory_has_items(target) == null || target~'type' == 'player', _error('Invalid container!'));
    if(!inventory_has_items(target), _error('Cant\'t create a table from an empty container!'));
    items = {};
    loop(inventory_size(target), i = inventory_get(target, _); if(i, [item, count, nbt] = i; items += [item, nbt]));
    createTable(name, keys(items));
);

createFromArea(name, from_pos, to_pos) -> (
    items = {};
    ignored_blocks = {};
    volume(from_pos, to_pos, item = _getItemFromBlock(_); if(item, items += item, ignored_blocks += str(_)));
    if(ignored_blocks, print(format('f » ', str('g The following blocks were ignored: %s ', join(', ', keys(ignored_blocks))))));
    if(!items, _error('The selected area is empty'));
    createTable(name, map(keys(items), [_, null]));
);

createTable(name, items) -> (
    if(list_files('', 'text')~name != null, _error('A table with that name exists already'));
    write_file(name, 'text', map(items, _:0 + (_:1 || '')));
    print(format('f » ', 'g Successfully created the ', str('#9B59B6 %s', name), 'g  table'));
    run(str('playsound minecraft:entity.evoker.cast_spell master %s', player()~'command_name'));
);

cloneTable(name, table) -> (
    if(list_files('', 'text')~table == null, _error(str('The table %s doesn\'t exist', table)));
    if(list_files('', 'text')~name != null, _error('A table with that name exists already'));
    createTable(name, _readTable(table));
    run(str('playsound minecraft:entity.evoker.cast_spell master %s', player()~'command_name'));
);

deleteTable(table, confirmation) -> (
    if(list_files('', 'text')~table == null, _error(str('The table %s doesn\'t exist', table)));
    if(!confirmation, exit(print(format('f » ', 'g Do you really want to delete the ', str('#9B59B6 %s', table), 'g  table? ', 'lb YES', '^l Click to confirm', str('!/%s delete %s confirm', global_app_name, table)))));
    delete_file(table, 'text');
    print(format('f » ', 'g Successfully deleted the ', str('#9B59B6 %s', table), 'g  table'));
    run(str('playsound minecraft:item.shield.break master %s', player()~'command_name'));
);

listTables() -> print(format(reduce(list_files('', 'text'), [..._a, if(_i == 0, '', 'g , '), str('#9B59B6 %s', _), str('^g /%s info %s', global_app_name, _), str('?/%s info %s', global_app_name, _)], ['f » ', 'g Available tables: '])));

info(table) -> (
    if(list_files('', 'text')~table == null, _error(str('The table %s doesn\'t exist', table)));
    items = _readTable(table);
    print(format(reduce(items, [item, nbt] = _; [..._a, if(length(items) < 5, ' \n', if(_i == 0, '', 'g , ')), str('g %s', if(length(items) < 5, '   ', '')), if(nbt, ...[str('g %s*', item), str('^g %s', nbt)], str('g %s', item)), str('!/give @s %s', item + (nbt || ''))], ['f » ', 'g The ', str('#9B59B6 %s', table), 'g  table contains the following items: '])));
);

view(table) -> (
    if(list_files('', 'text')~table == null, _error(str('The table %s doesn\'t exist', table)));

    items = _readTable(table);
    l = length(items);
    pages = map(range(l / 45), slice(items, _i * 45, min(l, (_i + 1) * 45)));
    global_page = 0;

    screen = create_screen(player(), 'generic_9x6', table, _(screen, player, action, data, outer(pages), outer(l)) -> (
        if(action == 'pickup' && length(pages) > 1,
            page = if(data:'slot' == 48, pages:(global_page += -1), data:'slot' == 50, pages:(global_page += 1));
            if(page,
                _setInfo(screen, global_page, length(pages), l);
                _setItems(screen, page);
            );
        );
        if(data:'slot' >= 45 && data:'slot' <= 53, return('cancel'));
    ));

    _setItems(screen, pages:global_page);

    _setInfo(screen, global_page, length(pages), l);
    if(length(pages) > 1,
        inventory_set(screen, 48, 1, 'spectral_arrow', '{display:{Name:\'{"text":"Previous page","color":"#9B59B6","italic":false}\'}}');
        inventory_set(screen, 50, 1, 'spectral_arrow', '{display:{Name:\'{"text":"Next page","color":"#9B59B6","italic":false}\'}}');
    );
    for(range(45, 54), if(!inventory_get(screen, _), inventory_set(screen, _, 1, 'gray_stained_glass_pane', '{display:{Name:\'""\'}}')));
);

editTable(table) -> (
    if(list_files('', 'text')~table == null, _error(str('The table %s doesn\'t exist', table)));
    print(format(reduce(_readTable(table), [item, nbt] = _; item_string = item + (nbt || ''); [..._a, ' \n  ', '#EB4D4Bb ❌', '^r Remove item', str('?/%s edit %s remove items %s', global_app_name, table, item_string), '  ', if(nbt, ...[str('g %s*', item), str('^g %s', nbt)], str('g %s', item)), str('!/give @s %s', item_string)], ['f » ', 'g Edit table ', str('#9B59B6 %s', table), 'g :   ', '#26DE81b ⟮+⟯', '^l Add more items', str('?/%s edit %s add items', global_app_name, table)])));
);

addItems(table, items_string) -> (
    addEntries(table, map(split(' ', items_string), [_, null]));
);

addFromTable(table, other_table) -> (
    if(list_files('', 'text')~other_table == null, _error(str('The table %s doesn\'t exist', other_table)));
    addEntries(table, _readTable(other_table));
);

addEntries(table, entries) -> (
    if(list_files('', 'text')~table == null, _error(str('The table %s doesn\'t exist', table)));
    table_items = _readTable(table);
    items = {};
    invalid_items = {};
    duplicate_items = {};
    for(entries, item = _:0; if(global_item_list~item == null, invalid_items += item, table_items~_ == null, items += _, duplicate_items += _));
    if(invalid_items, _error(str('Invalid items: %s', join(', ', keys(invalid_items)))));
    if(!items, _error('No items to add to the table'));
    write_file(table, 'text', map(keys(items), _:0 + (_:1 || '')));
    if(duplicate_items, print('f » ', str('g The following items were ignored as they were already in the table: %s', join(', ', keys(duplicate_items)))));
    print(format('f » ', 'g Successfully added the items to the ', str('#9B59B6 %s', table), 'g  table'));

);

removeItems(table, items_string) -> (
    removeEntries(table, map(split(' ', items_string), [_, null]));
);

removeFromTable(table, other_table) -> (
    if(list_files('', 'text')~other_table == null, _error(str('The table %s doesn\'t exist', other_table)));
    removeEntries(table, _readTable(other_table));
);

removeEntries(table, entries) -> (
    if(list_files('', 'text')~table == null, _error(str('The table %s doesn\'t exist', table)));
    table_items = _readTable(table);
    entries = map(entries, _:0);
    items = filter(table_items, entries~(_:0) == null);
    delete_file(table, 'text');
    write_file(table, 'text', map(items, _:0 + (_:1 || '')));
    print(format('f » ', 'g Successfully removed the items from the ', str('#9B59B6 %s', table), 'g  table'));
);

insert(mode, table, position) -> (
    if(list_files('', 'text')~table == null, _error(str('The table %s doesn\'t exist', table)));
    target = if(position, block(position), player()~'trace');
    if(!target || inventory_has_items(target) == null || target~'type' == 'player', _error('Invalid container!'));
    items = _readTable(table);
    if(mode~'shulker',
        loop(inventory_size(target), inventory_set(target, _, 1, 'white_shulker_box', {'BlockEntityTag' -> {'Items' -> map(range(27), [item, nbt] = rand(items); {'Slot' -> _i, 'id' -> item, 'tag' -> nbt, 'Count' -> if(mode == 'shulker_full', 64, floor(rand(stack_limit(item) - 1)) + 1)})}})),
        loop(if(mode~'single', 1, inventory_size(target)), [item, nbt] = rand(items); inventory_set(target, _, if(mode == 'random' || mode == 'single_random', rand(stack_limit(item) - 1) + 1, mode == 'single', 1, stack_limit(item)), item, nbt))
    );
    print(format('f » ', 'g Inserted ', str('#9B59B6 %s', table), str('g  table in %s at ' + pos(target), target)));
);

exportDatapack(mode, table) -> (
    if(list_files('', 'text')~table == null, _error(str('The table %s doesn\'t exist', table)));
    datapack_name = str('randomizer_%s_%s', table, mode);
    loot_name = str('%s.json', table);
    loot_mode = if(mode == 'shulker_full', 'random_stacks', mode == 'shulker_random', 'random', mode);
    data = {
        loot_mode -> {
            'loot_tables' -> {
                loot_name -> {
                    'pools' -> [
                        {
                            'rolls' -> if(loot_mode~'single', 1, 27),
                            'entries' -> map(_readTable(table),
                                [item, nbt] = _;
                                entry = {
                                    'type' -> 'item',
                                    'name' -> item,
                                    'functions' -> [
                                        {
                                            'function' -> 'set_count',
                                            'count' -> if(loot_mode == 'random' || loot_mode == 'single_random', {'min' -> 1, 'max' -> stack_limit(item)}, mode == 'single', 1, stack_limit(item))
                                        }
                                    ]
                                };
                                if(nbt, entry:'functions' += {
                                    'function' -> 'set_nbt',
                                    'tag' -> nbt
                                });
                                entry
                            )
                        }
                    ]
                }
            }
        }
    };
    if(mode~'shulker', data:mode = {
        'loot_tables' -> {
            loot_name -> {
                'pools' -> [
                    {
                        'rolls' -> 27,
                        'entries' -> [
                            {
                                'type' -> 'item',
                                'name' -> 'white_shulker_box',
                                'functions' -> [
                                    {
                                        'function' -> 'set_loot_table',
                                        'type' -> 'white_shulker_box',
                                        'name' -> str('%s:%s', loot_mode, table)
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        }
    });
    create_datapack(datapack_name, {'data' -> data});
    print(format('f » ', 'g The ', str('#9B59B6 %s', table), 'g  table was exported as ', str('#9B59B6 %s.zip', datapack_name), 'g  with mode ', str('#9B59B6 %s', mode)));
);

defaultTables() -> (
    tables = {
        'all_items' -> global_item_list,
        'stackables' -> filter(global_item_list, stack_limit(_) != 1),
        'stackables_64' -> filter(global_item_list, stack_limit(_) == 64),
        'stackables_16' -> filter(global_item_list, stack_limit(_) == 16),
        'unstackables' -> filter(global_item_list, stack_limit(_) == 1),
        'no_shulkers' -> filter(global_item_list, !_~'shulker_box'),
    };
    for(tables, delete_file(_, 'text'); write_file(_, 'text', tables:_));
);

__on_start() -> defaultTables();