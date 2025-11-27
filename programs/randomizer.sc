// Item Randomizer by CommandLeo

global_color = '#9B59B6';
global_containers = [...filter(item_list(), _~'shulker_box' != null), 'chest', 'barrel', 'hopper', 'dropper', 'dispenser'];
global_modes = {
    'single',
    'single_random',
    'single_stack',
    'random',
    'random_stacks',
    'full',
    'box_single',
    'box_single_random',
    'box_single_stack',
    'box_random',
    'box_random_stacks',
    'box_full'
};
global_obtainabilities = {
    'everything' -> 'Everything',
    'main_storage' -> 'Main Storage',
    'survival_obtainables' -> 'Survival Obtainables'
};
global_stackabilities = {
    'stackables' -> [16, 64],
    '64_stackables' -> [64],
    '16_stackables' -> [16],
    'unstackables' -> [1]
};
global_error_messages = {
    'ALLITEMS_NOT_INSTALLED' -> 'You must install the allitems script to use this feature',
    'FILE_DELETION_ERROR' -> 'There was an error while deleting the file',
    'FILE_WRITING_ERROR' -> 'There was an error while writing the file',
    'INVALID_INDEX' -> 'Invalid index',
    'INVALID_ITEMS' -> 'Invalid items: %s',
    'ITEM_LIST_DOESNT_EXIST' -> 'The item list %s doesn\'t exist',
    'ITEM_LIST_EMPTY' -> 'The %s item list is empty',
    'NOT_A_CONTAINER' -> 'That block is not a container',
    'NOT_LOOKING_AT_ANY_BLOCK' -> 'You are not looking at any block',
    'NO_CONTAINER_FOUND' -> 'No container was found',
    'NO_ENTRIES_TO_REMOVE' -> 'No entries to remove from the table',
    'NO_ITEMS_FOUND' -> 'No items were found',
    'NO_ITEMS_PROVIDED' -> 'No items were provided',
    'NO_ITEMS_TO_ADD' -> 'No items to add to the item list',
    'NO_TABLES' -> 'There are no tables saved',
    'SELECTED_AREA_EMPTY' -> 'The selected area is empty',
    'TABLE_ALREADY_EXISTS' -> 'A table with that name already exists',
    'TABLE_DOESNT_EXIST' -> 'The table %s doesn\'t exist',
    'TABLE_EMPTY' -> 'The %s table is empty'
};

global_current_page = {};

_checkVersion(version) -> (
    regex = '(\\d+)\.(\\d+)\.(\\d+)';
    target_version = map(version~regex, number(_));
    scarpet_version = map(system_info('scarpet_version')~regex, number(_));
    return(scarpet_version >= target_version);
);

__config() -> {
    'commands' -> {
        '' -> 'menu',
        'help' -> 'help',

        'create <name> items <items>' -> 'createTableFromItems',
        'create <name> item_list <item_list>' -> 'createTableFromItemList',
        'create <name> all_items <obtainability>' -> ['createTableFromAllItems', null],
        'create <name> all_items <obtainability> <stackability>' -> 'createTableFromAllItems',
        'create <name> containers' -> ['createTableFromContainers', null, null],
        'create <name> containers <from_pos>' -> ['createTableFromContainers', null],
        'create <name> containers <from_pos> <to_pos>' -> 'createTableFromContainers',
        'create <name> area <from_pos> <to_pos>' -> 'createTableFromArea',
        'rename <table> <name>' -> 'renameTable',
        'clone <table> <name>' -> 'cloneTable',
        'delete <table>' -> ['deleteTable', false],
        'delete <table> confirm' -> ['deleteTable', true],
        'edit <table>' -> 'editTable',
        'edit <table> add items <items>' -> 'addEntriesToTableFromItems',
        'edit <table> add table <other_table>' -> 'addEntriesToTableFromTable',
        'edit <table> add containers' -> ['addEntriesToTableFromContainers', null, null],
        'edit <table> add containers <from_pos>' -> ['addEntriesToTableFromContainers', null],
        'edit <table> add containers <from_pos> <to_pos>' -> 'addEntriesToTableFromContainers',
        'edit <table> add area <from_pos> <to_pos>' -> 'addEntriesToTableFromArea',
        'edit <table> remove items <entries>' -> 'removeEntriesFromTableFromItems',
        'edit <table> remove table <other_table>' -> 'removeEntriesFromTableFromTable',
        'edit <table> remove index <index>' -> 'removeEntriesFromTableWithIndex',
        'list' -> 'listTables',
        'info <table>' -> 'infoTable',
        ...if(_checkVersion('1.4.57'), {'view <table>' -> 'viewTable'}, {}),

        'insert <mode> <table>' -> ['insert', null, null],
        'insert <mode> <table> <from_pos>' -> ['insert', null],
        'insert <mode> <table> <from_pos> <to_pos>' -> 'insert',

        'give <container> <mode> <table>' -> 'giveContainer'
    },
    'arguments' -> {
        'name' -> {
            'type' -> 'term',
            'suggest' -> [],
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
        'index' -> {
            'type' -> 'int',
            'min' -> 0,
            'suggester' -> _(args) -> (
                table = args:'table';
                items = _readTable(table);
                if(items, return([range(length(items))]));
            )
        },
        'entries' -> {
            'type' -> 'text',
            'suggester' -> _(args) -> (
                table = args:'table';
                input = args:'entries';
                entries = split(' ', input);
                items = filter(map(_readTable(table), _:0), entries~_ == null);
                if(entries && slice(input, -1) != ' ', delete(entries, -1));
                return(if(entries, map(items, str('%s %s', join(' ', entries), _)), items));
            ),
            'case_sensitive' -> false
        },
        'items' -> {
            'type' -> 'text',
            'suggester' -> _(args) -> (
                input = args:'items';
                items = split(' ', input);
                if(items && slice(input, -1) != ' ', delete(items, -1));
                return(if(items, map(item_list(), str('%s %s', join(' ', items), _)), item_list()));
            ),
            'case_sensitive' -> false
        },
        'mode' -> {
            'type' -> 'term',
            'options' -> keys(global_modes),
            'case_sensitive' -> false
        },
        'container' -> {
            'type' -> 'term',
            'options' -> global_containers,
            'case_sensitive' -> false
        },
        'from_pos' -> {
            'type' -> 'pos',
            'loaded' -> true
        },
        'to_pos' -> {
            'type' -> 'pos',
            'loaded' -> true
        },
        'item_list' -> {
            'type' -> 'term',
            'suggester' -> _(args) -> map(list_files('item_lists', 'shared_text'), slice(_, length('item_lists') + 1)),
            'case_sensitive' -> false
        },
        'obtainability' -> {
            'type' -> 'term',
            'options' -> keys(global_obtainabilities)
        },
        'stackability' -> {
            'type' -> 'term',
            'options' -> keys(global_stackabilities)
        }
    },
    'requires' -> {
        'carpet' -> '>=1.4.44'
    },
    'scope' -> 'global',
    'strict' -> true
};

// HELPER FUNCTIONS

_error(error) -> (
    print(format(str('r %s', error)));
    run('playsound block.note_block.didgeridoo master @s');
    exit();
);

_removeDuplicates(list) -> filter(list, list~_ == _i);

// UTILITY FUNCTIONS

_parseEntry(entry) -> (
    if(!entry, return([null, null]));

    i = split(entry)~'{';
    [item, nbt] = if(
        i == 0,
            [null, null],
        i != null,
            [lower(slice(entry, 0, i)) || null, parse_nbt(slice(entry, i))],
        // else
            [lower(entry), null]
    );
    return([item, nbt]);
);

_readTable(table) -> (
    entries = map(read_file(table, 'text'), _parseEntry(_));
    entries = filter(entries, _:0 && _:0 != 'air' && item_list()~(_:0) != null);
    return(entries);
);

_readItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    entries = filter(map(read_file(item_list_path, 'shared_text'), _parseEntry(_)), _:0);
    return(entries);
);

_itemToString(item_tuple) -> (
    [item, nbt] = item_tuple;
    return(item + if(nbt, encode_nbt(nbt, true), ''));
);

_itemToMap(slot, item, count, nbt) -> (
    if(
        system_info('game_pack_version') >= 33,
            {'slot' -> slot, 'item' -> {'id' -> item, 'count' -> count, if(nbt, 'components' -> nbt, ...{})}},
        // else
            {'Slot' -> slot, 'id' -> item, 'Count' -> count, if(nbt, 'tag' -> nbt, ...{})}
    );
);

_formatTextComponent(text_component) -> (
    return(
        if(
            system_info('game_pack_version') >= 62,
                text_component,
            // else
                encode_json(text_component)
        );
    );
);

_giveCommand(item, nbt) -> (
    if(type(nbt) == 'nbt', nbt = parse_nbt(nbt));
    command = 'give @s ' + item + if(nbt, if(system_info('game_pack_version') >= 33, '[' + join(',', map(nbt, _ + '=' + encode_nbt(nbt:_, true))) + ']', encode_nbt(nbt, true)), '');
    return(command);
);

_getItemFromBlock(block) -> (
    if(item_list()~block != null, return(str(block)));

    return(
        if(
            block == 'bamboo_sapling', 'bamboo',
            block == 'beetroots', 'beetroot',
            block == 'big_dripleaf_stem', 'big_dripleaf',
            block == 'carrots', 'carrot',
            block == 'cocoa', 'cocoa_beans',
            block == 'pitcher_crop', 'pitcher_plant',
            block == 'potatoes', 'potato',
            block == 'powder_snow', 'powder_snow_bucket',
            block == 'redstone_wire', 'redstone',
            block == 'sweet_berry_bush', 'sweet_berries',
            block == 'tall_seagrass', 'seagrass',
            block == 'torchflower_crop', 'torchflower',
            block == 'tripwire', 'string',
            block~'cake', 'cake',
            block~'cauldron', 'cauldron',
            block~'cave_vine', 'glow_berries',
            block~'melon', 'melon_seeds',
            block~'pumpkin', 'pumpkin_seeds',
            block~'azalea_bush', replace(replace(block, '_bush', ''), 'potted_', ''),
            block~'plant', replace(block, '_plant', ''),
            block~'potted', replace(block, 'potted_', ''),
            block~'wall', replace(block, 'wall_', '')
        )
    );
);

_getReadingComparators(pos) -> (
    comparators = [];
    map(['north', 'east', 'south', 'west'],
        block1 = block(pos_offset(pos, _, -1));
        if(
            block1 == 'comparator' && block_state(block1, 'facing') == _,
                comparators += block1,
            solid(block1) && inventory_has_items(block1) == null,
                block2 = block(pos_offset(block1, _, -1));
                if(block2 == 'comparator' && block_state(block2, 'facing') == _, comparators += block2);
        );
    );
    return(comparators);
);

_updateComparators(block) -> (
    for(_getReadingComparators(block), update(_));
);

_itemScreen(items, name) -> (
    pages = map(range(length(items) / 45), slice(items, _i * 45, min(length(items), (_i + 1) * 45)));

    _setMenuInfo(screen, page_count, pages_length, items_length) -> (
        name = _formatTextComponent({'text' -> str('Page %d/%d', page_count % pages_length + 1, pages_length), 'color' -> global_color, 'italic' -> false});
        lore = [_formatTextComponent({'text' -> str('%s entries', items_length), 'color' -> 'gray', 'italic' -> false})];
        inventory_set(screen, 49, 1, 'paper', encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> {'custom_name' -> name, 'lore' -> lore}, 'id' -> 'paper'}, {'display' -> {'Name' -> name, 'Lore' -> lore}}), true));
    );

    _setMenuItems(screen, page) -> (
        loop(45, [item, nbt] = page:_; inventory_set(screen, _, if(_ < length(page), 1, 0), item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true)));
    );

    global_current_page:player() = 0;

    screen = create_screen(player(), 'generic_9x6', name, _(screen, player, action, data, outer(pages), outer(items)) -> (
        if(length(pages) > 1 && action == 'pickup' && (data:'slot' == 48 || data:'slot' == 50),
            page = if(data:'slot' == 48, pages:(global_current_page:player += -1), data:'slot' == 50, pages:(global_current_page:player += 1));
            _setMenuInfo(screen, global_current_page:player, length(pages), length(items));
            _setMenuItems(screen, page);
        );
        if(action == 'pickup_all' || action == 'quick_move' || (action != 'clone' && data:'slot' != null && 0 <= data:'slot' <= 44) || (45 <= data:'slot' <= 53), return('cancel'));
    ));

    _setMenuItems(screen, pages:0);

    for(range(45, 54), inventory_set(screen, _, 1, 'gray_stained_glass_pane', if(system_info('game_pack_version') >= 33, {'components' -> if(system_info('game_pack_version') >= 64, {'tooltip_display' -> {'hide_tooltip' -> true}}, {'hide_tooltip' -> {}}), 'id' -> 'gray_stained_glass_pane'}, {'display' -> {'Name' -> _formatTextComponent('')}})));
    _setMenuInfo(screen, global_current_page:player(), length(pages), length(items));
    if(length(pages) > 1,
        previous_page_name = _formatTextComponent({'text' -> 'Previous Page', 'color' -> global_color, 'italic' -> false});
        next_page_name = _formatTextComponent({'text' -> 'Next Page', 'color' -> global_color, 'italic' -> false});
        inventory_set(screen, 48, 1, 'arrow', encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> {'custom_name' -> previous_page_name}, 'id' -> 'arrow'}, {'display' -> {'Name' -> previous_page_name}}), true));
        inventory_set(screen, 50, 1, 'arrow', encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> {'custom_name' -> previous_page_name}, 'id' -> 'arrow'}, {'display' -> {'Name' -> previous_page_name}}), true));
    );
);

_getRandomContents(mode, items, size) -> (
    if(!items, return([]));
    [item, nbt] = rand(items);

    return(if(
        mode == 'single',
            [[item, 1, nbt]],
        mode == 'single_random',
            [[item, ceil(rand(stack_limit(item))), nbt]],
        mode == 'single_stack',
            [[item, stack_limit(item), nbt]],
        mode == 'random',
            map(range(size), [item, nbt] = rand(items); [item, ceil(rand(stack_limit(item))), nbt]),
        mode == 'random_stacks',
            map(range(size), [item, nbt] = rand(items); [item, stack_limit(item), nbt]),
        mode == 'full',
            map(range(size), [item, stack_limit(item), nbt]),
        mode~'box', 
            map(range(size), item_maps = map(_getRandomContents(mode~'box_(.+)', items, 27), [item, count, nbt] = _; _itemToMap(_i, item, count, nbt)); ['white_shulker_box', 1, encode_nbt(if(system_info('game_pack_version') >= 33, {'container' -> item_maps}, {'BlockEntityTag' -> {'Items' -> item_maps}}), true)]),
        // else
            []
    ));
);

// MAIN

menu() -> (
    texts = [
       'fs ' + ' ' * 80, ' \n',
        str('%sb Item Randomizer', global_color), if(_checkVersion('1.4.57'), ...['@https://github.com/CommandLeo/scarpet/wiki/Item-Randomizer', '^g Click to visit the wiki']), 'g  by ', str('%sb CommandLeo', global_color), '^g https://github.com/CommandLeo', if(_checkVersion('1.4.57'), '@https://github.com/CommandLeo'),' \n\n',
        'g A tool to easily insert random items inside containers.', '  \n',
        'g Run ', str('%s /%s help', global_color, system_info('app_name')), str('!/%s help', system_info('app_name')), '^g Click to run the command', 'g  to see a list of all the commands.', '  \n',
        'fs ' + ' ' * 80
    ];
    print(format(texts));
);

help() -> (
    texts = [ 
        'fs ' + ' ' * 80, ' \n',
        '%color% /%app_name% create <name> items <items>', 'f ｜', 'g Creates a table from a list of items', ' \n',
        '%color% /%app_name% create <name> item_list <item_list>', 'f ｜', 'g Creates a table from an item list', ' \n',
        '%color% /%app_name% create <name> container [<pos>]', 'f ｜', 'g Creates a table from the items inside the container you are looking at or at the specified coords', ' \n',
        '%color% /%app_name% create <name> area <start> <end>', 'f ｜', 'g Creates a table from the blocks within the specified coordinates', ' \n',
        '%color% /%app_name% delete <table>', 'f ｜', 'g Deletes a table', ' \n',
        '%color% /%app_name% clone <table> <name>', 'f ｜', 'g Creates a new table with the content of another table', ' \n',
        '%color% /%app_name% list', 'f ｜', 'g Lists all existing tables', ' \n',
        '%color% /%app_name% info <table>', 'f ｜', 'g Displays information about a table', ' \n',
        if(_checkVersion('1.4.57'), ...['%color% /%app_name% view <table>', 'f ｜', 'g Displays the content of a table inside a fancy GUI', ' \n']),
        '%color% /%app_name% edit <table>', 'f ｜', 'g Prints a menu to edit a table', ' \n',
        '%color% /%app_name% edit <table> add items <items>', 'f ｜', 'g Adds items to a table', ' \n',
        '%color% /%app_name% edit <table> add table <table>', 'f ｜', 'g Adds items to a table from another table', ' \n',
        '%color% /%app_name% edit <table> remove items <items>', 'f ｜', 'g Removes items from a table', ' \n',
        '%color% /%app_name% edit <table> remove index <index>', 'f ｜', 'g Removes a specific item from a table', ' \n',
        '%color% /%app_name% edit <table> remove table <table>', 'f ｜', 'g Remove from a table the items contained in another table', ' \n',
        '%color% /%app_name% insert <mode> <table> [<pos>]', 'f ｜', 'g Inserts the table in the container you are looking at or at the specified coords', ' \n',
        '%color% /%app_name% export <mode> <table>', 'f ｜', 'g Exports the table as a loot table in a datapack', ' \n',
        'fs ' + ' ' * 80
    ];
    replacement_map = {'%app_name%' -> system_info('app_name'), '%color%' -> global_color};
    print(format(map(texts, reduce(pairs(replacement_map), replace(_a, ..._), _))));
);

// TABLES

createTableFromItems(name, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    createTable(name, items);
);

createTableFromItemList(name, item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str(global_error_messages:'ITEM_LIST_EMPTY', item_list)));

    createTable(name, items);
);

createTableFromAllItems(name, category, stackability) -> (
    items = map(item_list(), [_, null]);
    survival_unobtainable_items = system_variable_get('survival_unobtainable_items');
    junk_items = system_variable_get('junk_items');
    firework_rockets = map(range(3), ['firework_rocket', if(system_info('game_pack_version') >= 33, {'fireworks' -> {'flight_duration' -> _ + 1}}, {'Fireworks' -> {'Flight' -> _ + 1}})]);
    if((category == 'main_storage' || category == 'survival_obtainables') && (!survival_unobtainable_items || !junk_items), _error(global_error_messages:'ALLITEMS_NOT_INSTALLED'));
    if(category == 'main_storage', put(items, items~['firework_rocket', null], firework_rockets, 'extend'));
    items = filter(items,
        [item, nbt] = _;
        category_check = if(
            category == 'main_storage',
                survival_unobtainable_items~item == null && (junk_items~item == null || nbt != null) && item~'shulker_box' == null,
            category == 'survival_obtainables',
                survival_unobtainable_items~item == null,
            true
        );
        stackability_check = !stackability || global_stackabilities:stackability~stack_limit(item) != null;
        item != 'air' && category_check && stackability_check;
    );

    createTable(name, items);
);

createTableFromContainers(name, from_pos, to_pos) -> (
    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error(global_error_messages:'NOT_LOOKING_AT_ANY_BLOCK'));
        from_pos = trace;
    );
    if(!to_pos,
        if(inventory_has_items(from_pos) == null, _error(global_error_messages:'NOT_A_CONTAINER'));
        to_pos = from_pos;
    );

    items = [];
    container_found = false;

    volume(from_pos, to_pos,
        block = _;

        if(inventory_has_items(from_pos),
            container_found = true;
            loop(inventory_size(block), item_tuple = inventory_get(block, _); if(item_tuple, [item, count, nbt] = item_tuple; items += [item, if(system_info('game_pack_version') >= 33, nbt:'components', nbt)]));
        );
    );

    if(!container_found, _error(global_error_messages:'NO_CONTAINER_FOUND'));
    if(!items, _error(global_error_messages:'NO_ITEMS_FOUND'));

    createTable(name, _removeDuplicates(items));
);

createTableFromArea(name, from_pos, to_pos) -> (
    items = {};
    ignored_blocks = {};
    volume(from_pos, to_pos, item = _getItemFromBlock(_); if(item, items += [item, null], ignored_blocks += str(_)));

    if(ignored_blocks, print(format('f » ', str('g The following blocks were ignored: %s', join(', ', keys(ignored_blocks))))));
    if(!items, _error(global_error_messages:'SELECTED_AREA_EMPTY'));

    createTable(name, keys(items));
);

createTable(name, items) -> (
    if(list_files('', 'text')~name != null, _error(global_error_messages:'TABLE_ALREADY_EXISTS'));
    if(!items, _error(global_error_messages:'NO_ITEMS_PROVIDED'));

    invalid_items = filter(map(items, _:0), item_list()~_ == null);
    if(invalid_items, _error(str(global_error_messages:'INVALID_ITEMS', join(', ', sort(_removeDuplicates(invalid_items))))));

    success = write_file(name, 'text', map(items, _itemToString(_)));

    if(!success, _error(global_error_messages:'FILE_WRITING_ERROR'));
    print(format('f » ', 'g Successfully created the ', str('%s %s', global_color, name), str('^g /%s %s %s', system_info('app_name'), if(_checkVersion('1.4.57'), 'view', 'info'), name), str('!/%s %s %s', system_info('app_name'), if(_checkVersion('1.4.57'), 'view', 'info'), name), 'g  table'));
    run('playsound block.note_block.pling master @s');
);

renameTable(table, name) -> (
    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));
    if(list_files('', 'text')~name != null, _error(global_error_messages:'TABLE_ALREADY_EXISTS'));

    success1 = write_file(name, 'text', read_file(table, 'text'));
    if(!success1, _error(global_error_messages:'FILE_WRITING_ERROR'));
    success2 = delete_file(table, 'text');
    if(!success2, _error(global_error_messages:'FILE_DELETION_ERROR'));

    print(format('f » ', 'g Successfully renamed the ', str('%s %s', global_color, table), 'g  table to ', str('%s %s', global_color, name)));
    run('playsound block.note_block.pling master @s');
);

cloneTable(table, name) -> (
    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));
    if(list_files('', 'text')~name != null, _error(global_error_messages:'TABLE_ALREADY_EXISTS'));

    success = write_file(name, 'text', read_file(table, 'text'));
    if(!success, _error(global_error_messages:'FILE_WRITING_ERROR'));
    print(format('f » ', 'g Successfully cloned the ', str('%s %s', global_color, table), 'g  table'));
    run('playsound block.note_block.pling master @s');
);

deleteTable(table, confirmation) -> (
    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));

    if(!confirmation, exit(print(format('f » ', 'g Click ', str('%sbu here', global_color), str('^g /%s delete %s confirm', system_info('app_name'), table), str('!/%s delete %s confirm', system_info('app_name'), table),'g  to confirm the deletion of the ', str('%s %s', global_color, table), 'g  table'))));
    success = delete_file(table, 'text');

    if(!success, _error(global_error_messages:'FILE_DELETION_ERROR'));
    print(format('f » ', 'g Successfully deleted the ', str('%s %s', global_color, table), 'g  table'));
    run('playsound minecraft:item.shield.break master @s');
);

editTable(table) -> (
    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));

    items = _readTable(table);
    if(!items, _error(str(global_error_messages:'TABLE_EMPTY', table)));

    print(format(reduce(items, [item, nbt] = _; [..._a, ' \n  ', '#EB4D4Bb ❌', '^r Remove the item', str('?/%s edit %s remove index %d', system_info('app_name'), table, _i), '  ', str('g%s %s%s', if(item_list()~item == null, 's', ''), item, if(nbt, '*', '')), str('!/%s', _giveCommand(item, nbt)), if(item_list()~item == null, '^g Invalid item', nbt, str('^g %s', nbt), ''), str('!/%s', _giveCommand(item, nbt))], ['f » ', 'g Edit the ', str('%s %s', global_color, table), 'g  item list: ', '#26DE81b (+)', '^#26DE81 Add more items', str('?/%s edit %s add', system_info('app_name'), table)])));
);

addEntriesToTableFromItems(table, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    addEntriesToTable(table, items);
);

addEntriesToTableFromTable(table, other_table) -> (
    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));
    if(list_files('', 'text')~other_table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', other_table)));

    addEntriesToTable(table, _readTable(other_table));
);

addEntriesToTableFromContainers(table, from_pos, to_pos) -> (
    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error(global_error_messages:'NOT_LOOKING_AT_ANY_BLOCK'));
        from_pos = trace;
    );
    if(!to_pos,
        if(inventory_has_items(from_pos) == null, _error(global_error_messages:'NOT_A_CONTAINER'));
        to_pos = from_pos;
    );

    items = [];
    container_found = false;

    volume(from_pos, to_pos,
        block = _;

        if(inventory_has_items(from_pos),
            container_found = true;
            loop(inventory_size(block), item_tuple = inventory_get(block, _); if(item_tuple, [item, count, nbt] = item_tuple; items += [item, if(system_info('game_pack_version') >= 33, nbt:'components', nbt)]));
        );
    );

    if(!container_found, _error(global_error_messages:'NO_CONTAINER_FOUND'));
    if(!items, _error(global_error_messages:'NO_ITEMS_FOUND'));

    addEntriesToTable(table, _removeDuplicates(items));
);

addEntriesToTableFromArea(table, from_pos, to_pos) -> (
    items = {};
    ignored_blocks = {};
    volume(from_pos, to_pos, item = _getItemFromBlock(_); if(item, items += [item, null], ignored_blocks += str(_)));

    if(ignored_blocks, print(format('f » ', str('g The following blocks were ignored: %s', join(', ', keys(ignored_blocks))))));
    if(!items, _error(global_error_messages:'SELECTED_AREA_EMPTY'));

    addEntriesToTable(table, keys(items));
);

addEntriesToTable(table, entries) -> (
    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));

    items = _readTable(table);
    if(!items, _error(global_error_messages:'NO_ITEMS_TO_ADD'));

    invalid_items = filter(map(items, _:0), item_list()~_ == null);
    if(invalid_items, _error(str(global_error_messages:'INVALID_ITEMS', join(', ', sort(_removeDuplicates(invalid_items))))));

    success = write_file(table, 'text', map(entries, _itemToString(_)));

    if(!success, _error(global_error_messages:'FILE_WRITING_ERROR'));
    print(format('f » ', 'g Successfully added the items to the ', str('%s %s', global_color, table), 'g  table'));
    run('playsound block.note_block.pling master @s');
);

removeEntriesFromTableFromItems(table, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    removeEntriesFromTable(table, items);
);

removeEntriesFromTableFromTable(table, other_table) -> (
    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));
    if(list_files('', 'text')~other_table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', other_table)));

    removeEntriesFromTable(table, _readTable(other_table));
);

removeEntriesFromTable(table, entries) -> (
    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));

    items = _readTable(table);

    l1 = length(items);
    items = filter(items, [item, nbt] = _; entries~[item, nbt] == null && entries~[item, null] == null);
    l2 = length(items);
    if(l2 == l1, _error(global_error_messages:'NO_ENTRIES_TO_REMOVE'));

    delete_file(table, 'text');
    success = write_file(table, 'text', map(items, _itemToString(_)));

    if(!success, _error(global_error_messages:'FILE_WRITING_ERROR'));
    print(format('f » ', 'g Successfully removed the items from the ', str('%s %s', global_color, table), 'g  table'));
    run('playsound block.note_block.pling master @s');
);

removeEntriesFromTableWithIndex(table, index) -> (
    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));

    items = _readTable(table);
    if(index < 0 || index >= length(items), _error(global_error_messages:'INVALID_INDEX'));

    delete(items:index);

    delete_file(table, 'text');
    success = write_file(table, 'text', map(items, _itemToString(_)));

    if(!success, _error(global_error_messages:'FILE_WRITING_ERROR'));
    print(format('f » ', 'g Successfully removed the item from the ', str('%s %s', global_color, table), 'g  table'));
    run('playsound block.note_block.pling master @s');
);

listTables() -> (
    tables = list_files('', 'text');
    if(!tables, _error(global_error_messages:'NO_TABLES'));

    texts = reduce(tables,
        [..._a, if(_i == 0, '', 'g , '), str('%s %s', global_color, _), str('^g /%s %s %s', system_info('app_name'), if(_checkVersion('1.4.57'), 'view', 'info'), _), str('?/%s %s %s', system_info('app_name'), if(_checkVersion('1.4.57'), 'view', 'info'), _)],
        ['f » ', 'g Saved tables: ']
    );
    print(format(texts));
);

infoTable(table) -> (
    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));

    items = _readTable(table);
    if(!items, _error(str(global_error_messages:'TABLE_EMPTY', table)));

    texts = reduce(items, [item, nbt] = _; [..._a, if(length(items) < 12, ' \n', if(_i == 0, '', 'g , ')), str('g %s', if(length(items) < 12, '   ', '')), str('g%s %s%s', if(item_list()~item == null, 's', ''), item, if(nbt, '*', '')), str('!/%s', _giveCommand(item, nbt)), if(item_list()~item == null, '^g Invalid item', nbt, str('^g %s', nbt), '')], ['f » ', 'g The ', str('%s %s', global_color, table), 'g  table contains the following items: ']);
    print(format(texts));
);

viewTable(table) -> (
    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));

    entries = _readTable(table);
    items = filter(entries, [item, nbt] = _; item != 'air' && item_list()~item != null);
    if(!items, _error(str(global_error_messages:'TABLE_EMPTY', table)));

    _itemScreen(items, table);
);

// INSERTION

insert(mode, table, from_pos, to_pos) -> (
    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error(global_error_messages:'NOT_LOOKING_AT_ANY_BLOCK'));
        from_pos = trace;
    );
    if(!to_pos,
        if(inventory_has_items(from_pos) == null, _error(global_error_messages:'NOT_A_CONTAINER'));
        to_pos = from_pos;
    );

    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));

    items = _readTable(table);
    if(!items, _error(str(global_error_messages:'TABLE_EMPTY', table)));

    last_block = null;
    affected_blocks = volume(from_pos, to_pos,
        block = _;

        if(inventory_has_items(block) != null,
            size = inventory_size(block);
            loop(size, inventory_set(block, _, 0));
            for(_getRandomContents(mode, items, size), [item, count, nbt] = _; inventory_set(block, _i, count, item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true)));
            _updateComparators(block);
            last_block = block;
        );
    );

    if(
        affected_blocks == 0,
            _error(global_error_messages:'NO_CONTAINER_FOUND'),
        affected_blocks == 1,
            print(format('f » ', 'g Inserted the ', str('%s %s', global_color, table), 'g  table in ', str('gi %s', last_block), 'g  at ' + pos(last_block))),
        // else
            print(format('f » ', 'g Inserted the ', str('%s %s', global_color, table), 'g  table in ', str('%s %s', global_color, affected_blocks), str('g  container%s', if(affected_blocks == 1, '', 's'))));
    );
    run('playsound block.note_block.pling master @s');
);

giveContainer(container, mode, table) -> (
    if(list_files('', 'text')~table == null, _error(str(global_error_messages:'TABLE_DOESNT_EXIST', table)));

    items = _readTable(table);
    if(!items, _error(str(global_error_messages:'TABLE_EMPTY', table)));


    item_maps = map(_getRandomContents(mode, items, 27), _itemToMap(_i, ..._));
    run(_giveCommand(container, if(system_info('game_pack_version') >= 33, {'container' -> item_maps}, {'BlockEntityTag' -> {'Items' -> item_maps}})));

);
