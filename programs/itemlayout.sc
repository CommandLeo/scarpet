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
    'scope' -> 'global'
};

// HELPER FUNCTIONS

_error(error) -> (
    print(format(str('r %s', error)));
    run(str('playsound block.note_block.didgeridoo master %s', player()~'command_name'));
    exit();
);

// UTILITY FUNCTIONS

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
    if(!air(pos1), return(_getItemFromBlock(block(pos1))));

    item_frame = entity_area('item_frame', pos1 + [0.5, 0.5, 0.5], [0.5, 0.5, 0.5]):0 || entity_area('glow_item_frame', pos1 + [0.5, 0.5, 0.5], [0.5, 0.5, 0.5]):0;
    if(item_frame, return(query(item_frame, 'item'):0 || replace(query(item_frame, 'nbt', 'Item.id'), '.+:', '')));

    return(_getItemFromBlock(block(pos)));
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

// MAIN

menu() -> (
    texts = [
       'fs ' + ' ' * 80, ' \n',
        str('%sb Item Layout', global_color), if(_checkVersion('1.4.57'), ...['@https://github.com/CommandLeo/scarpet/wiki/Item-Layout', '^g Click to visit the wiki']), 'g  by ', str('%sb CommandLeo', global_color), '^g https://github.com/CommandLeo', if(_checkVersion('1.4.57'), '@https://github.com/CommandLeo'),' \n\n',
        'g A tool to save a layout of items from a row of blocks and item frames to a file.', '  \n',
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
    success = write_file(item_layout_path, 'shared_text', items);

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

    entries = read_file(item_layout_path, 'shared_text');
    items = filter(entries, _ != 'air' && item_list()~_ != null);
    if(!items, _error(str(global_error_messages:'ITEM_LAYOUT_EMPTY', item_layout)));

    pages = map(range(length(items) / 45), slice(items, _i * 45, min(length(items), (_i + 1) * 45)));

    _setMenuInfo(screen, page_count, pages_length, items_length) -> (
        name = str('\'{"text":"Page %d/%d","color":"%s","italic":false}\'', page_count % pages_length + 1, pages_length, global_color);
        lore = [str('\'{"text":"%s entries","color":"gray","italic":false}\'', items_length)];
        nbt = if(system_info('game_pack_version') >= 33, {'components' -> {'custom_name' -> name, 'lore' -> lore}, 'id' -> 'paper'}, {'display' -> {'Name' -> name, 'Lore' -> lore}});
        inventory_set(screen, 49, 1, 'paper', nbt);
    );

    _setMenuItems(screen, page) -> (
        loop(45, inventory_set(screen, _, if(_ < length(page), 1, 0), page:_));
    );

    global_current_page:player() = 0;

    screen = create_screen(player(), 'generic_9x6', item_layout, _(screen, player, action, data, outer(pages), outer(items)) -> (
        if(length(pages) > 1 && action == 'pickup' && (data:'slot' == 48 || data:'slot' == 50),
            page = if(data:'slot' == 48, pages:(global_current_page:player += -1), data:'slot' == 50, pages:(global_current_page:player += 1));
            _setMenuInfo(screen, global_current_page:player, length(pages), length(items));
            _setMenuItems(screen, page);
        );
        if(action == 'pickup_all' || action == 'quick_move' || (action != 'clone' && data:'slot' != null && 0 <= data:'slot' <= 44) || (45 <= data:'slot' <= 53), return('cancel'));
    ));

    _setMenuItems(screen, pages:0);

    for(range(45, 54), inventory_set(screen, _, 1, 'gray_stained_glass_pane', if(system_info('game_pack_version') >= 33, {'components' -> {'hide_tooltip' -> {}}, 'id' -> 'gray_stained_glass_pane'}, {'display' -> {'Name' -> '\'{"text":""}\''}})));
    _setMenuInfo(screen, global_current_page:player(), length(pages), length(items));
    if(length(pages) > 1,
        inventory_set(screen, 48, 1, 'arrow', if(system_info('game_pack_version') >= 33, {'components' -> {'custom_name' -> str('\'{"text":"Previous page","color":"%s","italic":false}\'', global_color)}, 'id' -> 'arrow'}, {'display' -> {'Name' -> str('\'{"text":"Previous page","color":"%s","italic":false}\'', global_color)}}));
        inventory_set(screen, 50, 1, 'arrow', if(system_info('game_pack_version') >= 33, {'components' -> {'custom_name' -> str('\'{"text":"Next page","color":"%s","italic":false}\'', global_color)}, 'id' -> 'arrow'}, {'display' -> {'Name' -> str('\'{"text":"Next page","color":"%s","italic":false}\'', global_color)}}));
    );
);

pasteItemLayout(item_layout, direction, item_frame_direction, item_frames_only) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = read_file(item_layout_path, 'shared_text');
    if(!items, _error(global_error_messages:'ITEM_LAYOUT_EMPTY'));

    origin_pos = player()~'pos';
    for(items,
        pos = pos_offset(origin_pos, direction, _i);
        set(pos, 'air');
        print(_);
        if(item_list()~_ == null, continue());
        if(!item_frames_only,
            if(block_list()~_ != null, continue(set(pos, _)));
            if(place_item(_, pos), continue());
        );
        if(global_default_block, set(pos, global_default_block));
        spawn('item_frame', pos_offset(pos, item_frame_direction), {'Facing' -> global_directions~item_frame_direction, 'Fixed' -> true, 'Item' -> {'id' -> _, 'Count' -> 1}});
    );

    print(format('f » ', 'g The ', str('%s %s', global_color, item_layout), 'g  item layout has been pasted'));
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