// Item Layout by CommandLeo

global_directions = ['down', 'up', 'north', 'south', 'west', 'east'];
global_default_block = load_app_data():'default_block';

__config() -> {
    'commands' -> {
        '' -> 'help',
        'save <from_pos> <to_pos> <direction> <name>' -> 'saveLayout',
        'paste <layout> <direction> <facing>' -> ['pasteLayout', false],
        'paste <layout> <direction> <facing> <item_frames_only>' -> 'pasteLayout',
        'delete <layout>' -> ['deleteLayout', false],
        'delete <layout> confirm' -> ['deleteLayout', true],
        'list' -> 'listLayouts',
        'view <layout>' -> 'viewLayout',
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
            'suggest' -> global_directions,
            'case_sensitive' -> false
        },
        'facing' -> {
            'type' -> 'term',
            'options' -> global_directions,
            'suggest' -> global_directions,
            'case_sensitive' -> false
        },
        'item_frames_only' -> {
            'type' -> 'bool'
        },
        'name' -> {
            'type' -> 'term',
            'suggest' -> []
        },
        'layout' -> {
            'type' -> 'term',
            'suggester' -> _(arg) -> map(list_files('item_layouts', 'shared_text'), slice(_, length('item_layouts') + 1)),
            'case_sensitive' -> false
        }
    },
    'requires' -> {
        'carpet' -> '>=1.4.57'
    },
    'scope' -> 'player'
};

// HELPER FUNCTIONS

_error(error) -> exit(print(format(str('r %s', error))));

_scanStrip(from_pos, to_pos) -> (
    [x1, y1, z1] = from_pos;
    [x2, y2, z2] = to_pos;
    [dx, dy, dz] = map(to_pos - from_pos, if(_ < 0, -1, 1));
    if(x1 != x2, return(map(range(x1, x2 + dx, dx), [_, y1, z1])));
    if(y1 != y2, return(map(range(y1, y2 + dy, dy), [x1, _, z1])));
    if(z1 != z2, return(map(range(z1, z2 + dz, dz), [x1, y1, _])));
);

_getItemAtPos(pos, direction) -> (
    pos1 = pos_offset(pos, direction);
    block1 = block(pos1);
    if(!air(block1), return(_getItemFromBlock(block1)));

    item_frame = entity_area('item_frame', block1, [0.5, 0.5, 0.5]):0 || entity_area('glow_item_frame', block1, [0.5, 0.5, 0.5]):0;
    if(item_frame, return(replace(query(item_frame, 'nbt', 'Item.id'), '.+:', '')));

    return(_getItemFromBlock(block(pos)));
);

_getItemFromBlock(block) -> (
    if(!block || block == global_default_block, return('air'));
    if(item_list()~block != null, return(block));

    [x, y, z] = pos(block);
    dummy_y = -1000;
    run(str('loot spawn %d %d %d mine %d %d %d shears{Enchantments:[{id:silk_touch,lvl:1}]}', x, dummy_y, z, x, y, z));
    items = entity_area('item', [x, dummy_y, z], [0.5, 0.5, 0.5]);
    item = items:(-1);
    for(items, modify(_, 'remove'));
    if(item, return(item~'item':0));
    return('air');
);

_setInfo(screen, page_count, pages_length, items_length) -> (
    inventory_set(screen, 49, 1, 'paper', str('{display:{Name:\'{"text":"Page %d/%d","color":"#26DE81","italic":false}\',Lore:[\'{"text":"%s entries","color":"gray","italic":false}\']}}', page_count % pages_length + 1, pages_length, items_length));
);

_setItems(screen, items) -> (
    loop(45, inventory_set(screen, _, if(_ < length(items), 1, 0), items:_));
);

// MAIN

help() -> (
    texts = [
        'fs ' + ' ' * 80, ' \n',
        '#1ECB74b Item Layout ', 'g by ', '#26DE81b CommandLeo', '^g https://github.com/CommandLeo', '@https://github.com/CommandLeo', ' \n\n',
        '#26DE81 /app_name save <from_pos> <to_pos> <direction> <name> ', 'f ｜ ', 'g Saves a layout from a row of blocks, looking for extra blocks or item frames in the specified direction', ' \n',
        '#26DE81 /app_name paste <layout> <direction> <item_frame_facing> [<item_frames_only>]', 'f ｜ ', 'g Pastes a layout starting from your current position toward the specified direction', ' \n',
        '#26DE81 /app_name delete <layout> ', 'f ｜ ', 'g Deletes a layout', ' \n',
        '#26DE81 /app_name list ', 'f ｜ ', 'g Lists all layouts', ' \n',
        '#26DE81 /app_name view <layout> ', 'f ｜ ', 'g Displays the content of a layout inside a fancy menu', ' \n',
        '#26DE81 /app_name default_block [get|set|reset] ', 'f ｜ ', 'g Gets, sets or resets the default block, which will be ignored when saving a layout', ' \n',
        'fs ' + ' ' * 80
    ];
    print(format(map(texts, replace(_, 'app_name', system_info('app_name')))));
);

saveLayout(from_pos, to_pos, direction, filename) -> (
    if(length(filter(to_pos - from_pos, _ == 0)) != 2, _error('The area must be a row of blocks'));

    items = map(_scanStrip(from_pos, to_pos), _getItemAtPos(_, direction));
    delete_file(str('item_layouts/%s', filename), 'shared_text');
    write_file(str('item_layouts/%s', filename), 'shared_text', items);
    print(format('f » ', 'g Item layout saved as ', str('#26DE81 %s', filename)));
);

deleteLayout(layout, confirm) -> (
    if(list_files('item_layouts', 'shared_text')~str('item_layouts/%s', layout) == null, _error('That item layout doesn\'t exist'));

    if(!confirm, exit(print(format('f » ', 'g Click ', '#26DE81u here', str('^g /%s delete %s confirm', system_info('app_name'), layout), str('!/%s delete %s confirm', system_info('app_name'), layout),'g  to confirm the deletion'))));
    delete_file(str('item_layouts/%s', layout), 'shared_text');
    print(format('f » ', 'g Item layout ', str('#26DE81 %s', layout), 'g  has been deleted'));
);

listLayouts() -> (
    files = list_files('item_layouts', 'shared_text');
    if(!files, _error('There are no item layouts saved'));

    layouts = map(files, slice(_, length('item_layouts') + 1));
    texts = reduce(layouts, [..._a, if(_i == 0, '', 'g , '), str('#26DE81 %s', _), str('?/%s view %s', system_info('app_name'), _)], ['f » ', 'g Saved item layouts: ']);
    print(format(texts));
);

pasteLayout(layout, direction, item_frame_direction, item_frames_only) -> (
    if(list_files('item_layouts', 'shared_text')~str('item_layouts/%s', layout) == null, _error('That item layout doesn\'t exist'));

    items = read_file(str('item_layouts/%s', layout), 'shared_text');
    if(!items, _error('There are no items in the item layout'));

    origin_pos = player()~'pos';
    for(items,
        pos = pos_offset(origin_pos, direction, _i);
        set(pos, 'air');
        if(item_list()~_ == null, continue());
        if(!item_frames_only,
            if(block_list()~_ != null, continue(set(pos, _)));
            if(place_item(_, pos), continue());
        );
        if(global_default_block, set(pos, global_default_block));
        spawn('item_frame', pos_offset(pos, item_frame_direction), {'Facing' -> global_directions~item_frame_direction, 'Fixed' -> true, 'Item' -> {'id' -> _, 'Count' -> 1}});
    );

    print(format('f » ', 'g Item layout ', str('#26DE81 %s', layout), 'g  has been pasted'));
);

viewLayout(layout) -> (
    if(list_files('item_layouts', 'shared_text')~str('item_layouts/%s', layout) == null, _error('That item layout doesn\'t exist'));

    entries = read_file(str('item_layouts/%s', layout), 'shared_text');
    items = filter(entries, _ != 'air' && item_list()~_ != null);
    l = length(items);
    if(!l, _error('There are no items in the item layout'));

    pages = map(range(l / 45), slice(items, _ * 45, min(l, (_ + 1) * 45)));
    global_page = 0;

    screen = create_screen(player(), 'generic_9x6', layout, _(screen, player, action, data, outer(pages), outer(l)) -> (
        if(action == 'pickup' && length(pages) > 1,
            page = if(data:'slot' == 48, pages:(global_page += -1), data:'slot' == 50, pages:(global_page += 1));
            if(page,
                _setInfo(screen, global_page, length(pages), l);
                _setItems(screen, page);
            );
        );
        if(45 <= data:'slot' <= 53, return('cancel'));
    ));

    _setItems(screen, pages:global_page);

    for(range(45, 54), inventory_set(screen, _, 1, 'gray_stained_glass_pane', '{display:{Name:\'""\'}}'));
    _setInfo(screen, global_page, length(pages), l);
    if(length(pages) > 1,
        inventory_set(screen, 48, 1, 'spectral_arrow', '{display:{Name:\'{"text":"Previous page","color":"#26DE81","italic":false}\'}}');
        inventory_set(screen, 50, 1, 'spectral_arrow', '{display:{Name:\'{"text":"Next page","color":"#26DE81","italic":false}\'}}');
    );
);

// DEFAULT BLOCK

getDefaultBlock() -> (
    if(global_default_block,
        print(format('f » ', 'g The default block is currently set to ', str('#26DE81 %s', global_default_block))),
        _error('There\'s no default block')
    );
);

setDefaultBlock(block) -> (
    global_default_block = block;
    if(block,
        print(format('f » ', 'g The default block has been set to ', str('#26DE81 %s', block))),
        print(format('f » ', 'g The default block has been reset'))
    );
);

// EVENTS

__on_close() -> (
    store_app_data({'default_block' -> global_default_block});
);