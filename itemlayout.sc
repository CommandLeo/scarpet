// Item Layout by CommandLeo

global_color = '#26DE81';
global_directions = ['down', 'up', 'north', 'south', 'west', 'east'];

global_default_block = load_app_data():'default_block' || 'gray_concrete';
global_error_messages = {
    'FILE_DELETION_ERROR' -> 'There was an error while deleting the file',
    'FILE_WRITING_ERROR' -> 'There was an error while writing the file',
    'ITEM_LAYOUT_ALREADY_EXISTS' -> 'A item layout with that name already exists',
    'ITEM_LAYOUT_DOESNT_EXIST' -> 'The item layout %s doesn\'t exist',
    'ITEM_LAYOUT_EMPTY' -> 'The %s item layout is empty',
    'NOT_A_ROW_OF_BLOCKS' -> 'The area must be a row of blocks',
    'NO_DEFAULT_BLOCK' -> 'There is no default block',
    'NO_ITEM_LAYOUTS' -> 'There are no item layouts saved'
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

        'save <from_pos> <to_pos> <direction> <name>' -> 'saveItemLayout',
        'paste <item_layout> <direction> <facing>' -> ['pasteItemLayout', false],
        'paste <item_layout> <direction> <facing> <item_frames_only>' -> 'pasteItemLayout',
        'delete <item_layout>' -> ['deleteItemLayout', false],
        'delete <item_layout> confirm' -> ['deleteItemLayout', true],
        'list' -> 'listItemLayouts',
        ...if(_checkVersion('1.4.57'), {'view <item_layout>' -> 'viewItemLayout'}, {}),
        'default_block' -> 'getDefaultBlock',
        'default_block get' -> 'getDefaultBlock',
        'default_block set <block>' -> 'setDefaultBlock',
        'default_block reset' -> ['setDefaultBlock', null],
    },
    'arguments' -> {
        'from_pos' -> {
            'type' -> 'pos',
            'loaded' -> true
        },
        'to_pos' -> {
            'type' -> 'pos',
            'loaded' -> true
        },
        'direction' -> {
            'type' -> 'term',
            'options' -> global_directions,
            'case_sensitive' -> false
        },
        'facing' -> {
            'type' -> 'term',
            'options' -> global_directions,
            'case_sensitive' -> false
        },
        'item_frames_only' -> {
            'type' -> 'bool'
        },
        'name' -> {
            'type' -> 'term',
            'suggest' -> [],
            'case_sensitive' -> false
        },
        'item_layout' -> {
            'type' -> 'term',
            'suggester' -> _(args) -> map(list_files('item_layouts', 'shared_text'), slice(_, length('item_layouts') + 1)),
            'case_sensitive' -> false
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
    run(str('playsound block.note_block.didgeridoo master %s', player()~'command_name'));
    exit();
);

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

_itemToString(item_tuple) -> (
    [item, nbt] = item_tuple;
    return(item + if(nbt, encode_nbt(nbt, true), ''));
);

_readItemLayout(item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    entries = map(read_file(item_layout_path, 'shared_text'), _parseEntry(_));
    entries = filter(entries, _:0);
    return(entries);
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

_scanStrip(from_pos, to_pos) -> (
    [x1, y1, z1] = from_pos;
    [x2, y2, z2] = to_pos;
    [dx, dy, dz] = map(to_pos - from_pos, if(_ < 0, -1, 1));

    if(
        x1 != x2,
            return(map(range(x1, x2 + dx, dx), block([_, y1, z1]))),
        y1 != y2,
            return(map(range(y1, y2 + dy, dy), block([x1, _, z1]))),
        z1 != z2,
            return(map(range(z1, z2 + dz, dz), block([x1, y1, _])))
    );
    return([block(from_pos)]);
);

_getItemAtPos(pos, direction) -> (
    pos1 = pos_offset(pos, direction);
    if(!air(pos1), return([_getItemFromBlock(block(pos1)), null]));

    item_frame_types = filter(entity_types(), _~'item_frame');
    for(item_frame_types,
        item_frame = entity_area(_, pos1 + [0.5, 0.5, 0.5], [0.5, 0.5, 0.5]):0;
        if(item_frame,
            item_tuple = query(item_frame, 'item');
            if(item_tuple,
                [item, count, nbt] = item_tuple;
                return([item, nbt]);
            );
            item_data = query(item_frame, 'nbt', 'Item');
            if (item_data,
                item = split(':', item_data:'id'):(-1);
                nbt = item_data:'tag' || item_data:'components';
                return([item, nbt])
            );
        );
    );

    return([_getItemFromBlock(block(pos)), null]);
);

_getItemFromBlock(block) -> (
    if(!block || block == global_default_block, return('air'));
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
            block~'wall', replace(block, 'wall_', ''),
            'air'
        )
    );
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

// MAIN

menu() -> (
    texts = [
       'fs ' + ' ' * 80, ' \n',
        str('%sb Item Layout', global_color), if(_checkVersion('1.4.57'), ...['@https://github.com/CommandLeo/scarpet/wiki/Item-Layout', '^g Click to visit the wiki']), 'g  by ', str('%sb CommandLeo', global_color), '^g https://github.com/CommandLeo', if(_checkVersion('1.4.57'), '@https://github.com/CommandLeo'),' \n\n',
        'g A script that allows saving an item layout in the form of a row of blocks and item frames to a file.', '  \n',
        'g Run ', str('%s /%s help', global_color, system_info('app_name')), str('!/%s help', system_info('app_name')), '^g Click to run the command', 'g  to see a list of all the commands.', '  \n',
        'fs ' + ' ' * 80
    ];
    print(format(texts));
);

help() -> (
    texts = [
        'fs ' + ' ' * 80, ' \n',
        '%color% /%app_name% save <from_pos> <to_pos> <direction> <name>', 'f ｜', 'g Saves an item layout from a row of blocks, looking for extra blocks or item frames in the specified direction', ' \n',
        '%color% /%app_name% paste <item_layout> <direction> <item_frame_facing> [<item_frames_only>]', 'f ｜', 'g Pastes an item layout starting from your current position toward the specified direction, with the item frames facing the specified direction', ' \n',
        '%color% /%app_name% delete <item_layout>', 'f ｜', 'g Deletes an item layout', ' \n',
        '%color% /%app_name% list', 'f ｜', 'g Lists all item layouts', ' \n',
        if(_checkVersion('1.4.57'), ...['%color% /%app_name% view <item_layout>', 'f ｜', 'g Displays the contents of an item layout inside a fancy menu', ' \n']),
        '%color% /%app_name% default_block [get|set|reset]', 'f ｜', 'g Gets, sets or resets the default block, the block type that will be ignored when saving an item layout', ' \n',
        'fs ' + ' ' * 80
    ];
    replacement_map = {'%app_name%' -> system_info('app_name'), '%color%' -> global_color};
    print(format(map(texts, reduce(pairs(replacement_map), replace(_a, ..._), _))));
);

saveItemLayout(from_pos, to_pos, direction, name) -> (
    item_layout_path = str('item_layouts/%s', name);
    if(list_files('item_layouts', 'shared_text')~item_layout_path != null, _error(global_error_messages:'ITEM_LAYOUT_ALREADY_EXISTS'));
    if(length(filter(to_pos - from_pos, _ == 0)) != 2, _error(global_error_messages:'NOT_A_ROW_OF_BLOCKS'));

    items = map(_scanStrip(from_pos, to_pos), _getItemAtPos(_, direction));
    success = write_file(item_layout_path, 'shared_text', map(items, _itemToString(_)));

    if(!success, _error(global_error_messages:'FILE_WRITING_ERROR'));
    print(format('f » ', 'g Successfully saved the ', str('%s %s', global_color, name), str('^g /%s view %s', system_info('app_name'), name), str('!/%s view %s', system_info('app_name'), name), 'g  item layout'));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

deleteItemLayout(item_layout, confirmation) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    if(!confirmation, exit(print(format('f » ', 'g Click ', str('%sbu here', global_color), str('^g /%s delete %s confirm', system_info('app_name'), item_layout), str('!/%s delete %s confirm', system_info('app_name'), item_layout),'g  to confirm the deletion of the ', str('%s %s', global_color, item_layout), 'g  item layout'))));

    success = delete_file(item_layout_path, 'shared_text');

    if(!success, _error(global_error_messages:'FILE_DELETION_ERROR'));
    print(format('f » ', 'g Successfully deleted the ', str('%s %s', global_color, item_layout), 'g  item layout'));
    run(str('playsound item.shield.break master %s', player()~'command_name'));
);

listItemLayouts() -> (
    files = list_files('item_layouts', 'shared_text');
    if(!files, _error(global_error_messages:'NO_ITEM_LAYOUTS'));

    item_layouts = map(files, slice(_, length('item_layouts') + 1));

    texts = reduce(item_layouts,
        [..._a, if(_i == 0, '', 'g , '), str('%s %s', global_color, _), if(_checkVersion('1.4.57'), ...[str('^g /%s view %s', system_info('app_name'), _), str('?/%s view %s', system_info('app_name'), _)])],
        ['f » ', 'g Saved item layouts: ']
    );
    print(format(texts));
);

viewItemLayout(item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = _readItemLayout(item_layout);
    if(!items, _error(str(global_error_messages:'ITEM_LAYOUT_EMPTY', item_layout)));

    _itemScreen(items, item_layout);
);

pasteItemLayout(item_layout, direction, item_frame_facing, item_frames_only) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = _readItemLayout(item_layout);
    if(!items, _error(global_error_messages:'ITEM_LAYOUT_EMPTY'));

    origin_pos = player()~'pos';
    for(items,
        [item, nbt] = _;
        pos = pos_offset(origin_pos, direction, _i);
        set(pos, 'air');
        if(item_list()~item == null, continue());
        if(!item_frames_only,
            if(block_list()~item != null, continue(set(pos, item)));
            if(place_item(item, pos), continue());
        );
        if(global_default_block, set(pos, global_default_block));
        item_frame_pos = pos_offset(pos, item_frame_facing);
        facing = global_directions~item_frame_facing;
        spawn('item_frame', item_frame_pos, {'Facing' -> facing, 'Fixed' -> true, 'Item' -> if(system_info('game_pack_version') >= 33, {'id' -> item, 'components' -> nbt}, {'id' -> item, 'Count' -> 1, ...if(nbt, {'tag' -> nbt}, {})})});
    );

    print(format('f » ', 'g The ', str('%s %s', global_color, item_layout), 'g  item layout has been pasted'));
    run('playsound block.note_block.pling master @s');
);

// DEFAULT BLOCK

getDefaultBlock() -> (
    if(global_default_block,
        print(format('f » ', 'g The default block is currently set to ', str('%s %s', global_color, global_default_block))),
        _error(global_error_messages:'NO_DEFAULT_BLOCK')
    );
);

setDefaultBlock(block) -> (
    global_default_block = block;
    if(block,
        print(format('f » ', 'g The default block has been set to ', str('%s %s', global_color, block))),
        print(format('f » ', 'g The default block has been reset'))
    );
);

// EVENTS

__on_close() -> (
    if(global_default_block, store_app_data({'default_block' -> global_default_block}));
);