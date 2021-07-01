// Item Randomizer by CommandLeo

global_item_list = item_list();

__config() -> {
    'commands' -> {
        '' -> 'help',
        'create <name> items <items>' -> 'create',
        'create <name> fromContainer' -> ['createFromContainer', null],
        'create <name> fromContainer <pos>' -> 'createFromContainer',
        'create <name> fromArea <start> <end>' -> 'createFromArea',
        'delete <table>' -> 'deleteTable',
        'merge <table> <other_table> <name>' -> 'mergeTables',
        'list' -> 'listTables',
        'info <table>' -> 'info',
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
                i = args:'items' || '';
                items = split(' ', i);
                if(length(items) && split('', i):(-1) != ' ', delete(items, -1));
                if(items, map(global_item_list, str('%s %s', join(' ', items), _)), global_item_list);
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
        'start' -> {
            'type' -> 'pos'
        },
        'end' -> {
            'type' -> 'pos'
        },
        'randomAmount' -> {
            'type' -> 'bool'
        }
    },
    'scope' -> 'global'
};

help() -> (
    print(format(
        'fs -----------------------------------------------------', ' \n',
        '#8E44ADb Item Randomizer ', '#9B59B6 by ', '#9B59B6b CommandLeo', ' \n\n',
        '#9B59B6 /randomizer create <name> items <items> ', 'f ｜ ', 'g Creates a table from a list of items', ' \n',
        '#9B59B6 /randomizer create <name> fromContainer [<pos>] ', 'f ｜ ', 'g Creates a table from the items inside the container you are looking at or at the specified coords', ' \n',
        '#9B59B6 /randomizer create <name> fromArea <start> <end> ', 'f ｜ ', 'g Creates a table from the blocks within the specified coordinates', ' \n',
        '#9B59B6 /randomizer delete <table> ', 'f ｜ ', 'g Deletes a table', ' \n',
        '#9B59B6 /randomizer merge <table1> <table2> <name>', 'f ｜ ', 'g Merges two tables into a new one', ' \n',
        '#9B59B6 /randomizer list ', 'f ｜ ', 'g Lists all existing tables', ' \n',
        '#9B59B6 /randomizer info <table> ', 'f ｜ ', 'g Displays information about a table', ' \n',
        '#9B59B6 /randomizer insert <mode> <table> [<pos>] ', 'f ｜ ', 'g Inserts the table in the container you are looking at or at the specified coords', ' \n',
        '#9B59B6 /randomizer export <mode> <table> ', 'f ｜ ', 'g Exports the table as a loot table in a datapack', ' \n',
        'fs -----------------------------------------------------'
    ));
);

readTable(table) -> filter(read_file(table, 'text'), global_item_list~_ != null);

create(name, item_string) -> (
    items = split(' ', item_string);
    invalid_items = filter(items, global_item_list~_ == null);
    if(length(invalid_items) > 0, exit(print(str('§cInvalid items: %s', join(', ', invalid_items)))));
    createTable(name, items);
);

createFromContainer(name, position) -> (
    b = position || query(player(), 'trace', 5, 'blocks');
    if(!b || inventory_has_items(b) == null, exit(print('§cThat block is not a container!')));
    if(!inventory_has_items(b), exit(print('§cCant\'t create a table from an empty container')));
    items = {};
    loop(inventory_size(b), item = inventory_get(b, _):0; if(item, items += item));
    createTable(name, keys(items));
);

createFromArea(name, start, end) -> (
    items = {};
    ignored_blocks = {};
    volume(start, end, if(global_item_list~_ == null || air(_), ignored_blocks += str(_), items += str(_)));
    if(!items, exit(print('§cThe selected area is empty')));
    if(ignored_blocks, print(str('§8» §7The following blocks were ignored: %s', join(' ', keys(ignored_blocks)))));
    createTable(name, keys(items));
);

createTable(name, items) -> (
    if(list_files('', 'text')~name != null, exit(print('§cThat name is already in use!')));
    write_file(name, 'text', items);
    print('§aTable was successfully created');
    run(str('playsound minecraft:entity.evoker.cast_spell master %s', player()));
);

deleteTable(table) -> (
    if(list_files('', 'text')~table == null, exit(print('§cThat table doesn\'t exist')));
    delete_file(table, 'text');
    print(str('§aTable %s was deleted', table));
    run(str('minecraft:item.shield.break master %s', player()));
);

mergeTables(table1, table2, name) -> (
    if(list_files('', 'text')~table1 == null, exit(print(str('§c%s doesn\'t exist', table1))));
    if(list_files('', 'text')~table2 == null, exit(print(str('§c%s doesn\'t exist', table2))));
    if(table1 == table2, exit(print('§cThe two tables can\'t be the same')));
    if(list_files('', 'text')~name != null, exit(print('§cThat name is already in use!')));
    items1 = readTable(table1);
    items2 = readTable(table2);
    items = {};
    for(items1, items += _);
    for(items2, items += _);
    createTable(name, keys(items));
);

listTables() -> print(format(reduce(list_files('', 'text'), [..._a, if(_i == 0, '', 'g , '), str('#9B59B6 %s', _), str('^g /%s info %s', system_info('app_name'), _), str('?/%s info %s', system_info('app_name'), _)], ['f » ', 'g Available tables: '])));

info(table) -> (
    if(list_files('', 'text')~table == null, exit(print('§cThat table doesn\'t exist')));
    items = readTable(table);
    print(format(reduce(items, [..._a, if(length(items) < 10, ' \n', if(_i == 0, '', 'g , ')), str('g %s%s', if(length(items) < 10, '   ', ''),_), str('!/give @s %s', _)], ['f » ', 'g The table ', str('#9B59B6 %s ', table), 'g contains the following items: '])));
);

insert(mode, table, position) -> (
    if(list_files('', 'text')~table == null, exit(print('§cThat table doesn\'t exist')));
    b = position || pos(query(player(), 'trace', 5, 'blocks'));
    if(!b || inventory_has_items(b) == null, exit(print('§cThat block is not a container!')));
    items = readTable(table);
    if(mode~'shulker',
        loop(inventory_size(b), inventory_set(b, _, 1, 'white_shulker_box', {'BlockEntityTag' -> {'Items' -> map(range(27), item = rand(items); {'Slot' -> _i, 'id' -> item, 'Count' -> if(mode == 'shulker_full', 64, floor(rand(stack_limit(item) - 1)) + 1)})}})),
        loop(if(mode~'single', 1, inventory_size(b)), item = rand(items); inventory_set(b, _, if(mode == 'random' || mode == 'single_random', rand(stack_limit(item) - 1) + 1, mode == 'single', 1, stack_limit(item)), item))
    );
    print(format('f » ', 'g Table ', str('#9B59B6 %s ', table), str('g was inserted in %s at ' + b, block(b))));
);

exportDatapack(mode, table) -> (
    if(list_files('', 'text')~table == null, exit(print('§cThat table doesn\'t exist')));
    datapack_name = str('random_%d', time());
    loot_name = str('%s.json', table);
    loot_mode = if(mode == 'shulker_full', 'random_stacks', mode == 'shulker_random', 'random', mode);
    data = {
        loot_mode -> {
            'loot_tables' -> {
                loot_name -> {
                    'pools' -> [
                        {
                            'rolls' -> if(loot_mode~'single', 1, 27),
                            'entries' -> map(readTable(table), {'type' -> 'item', 'name' -> _, 'functions' -> [{'function' -> 'set_count', 'count' -> if(loot_mode == 'random' || loot_mode == 'single_random', {'min' -> 1, 'max' -> stack_limit(_)}, mode == 'single', 1, stack_limit(_))}]})
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
    print(format('f » ', 'g Table ', str('#9B59B6 %s', table), 'g  was exported as ', str('#9B59B6 %s.zip', datapack_name), 'g  with mode ', str('#9B59B6 %s', mode)));
);

defaultTables() -> (
    tables = {
        'all_items' -> global_item_list,
        'stackables_64' -> filter(global_item_list, stack_limit(_) == 64),
        'stackables_16' -> filter(global_item_list, stack_limit(_) == 16),
        'unstackables' -> filter(global_item_list, stack_limit(_) == 1),
    };
    for(tables, if(list_files('', 'text')~_ == null, write_file(_, 'text', tables:_)));
);

defaultTables();
