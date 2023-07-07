// Fill Level Utilities by CommandLeo

global_color = '#E67E22';

global_stackable_dummy_item = global_default_stackable_dummy_item = ['structure_void', null];
global_unstackable_dummy_item = global_default_unstackable_dummy_item = ['shulker_box', null];

__config() -> {
    'commands' -> {
        '' -> 'help',

        'get' -> ['printFillLevel', null],
        'get <pos>' -> 'printFillLevel',
        'set <fill_level>' -> ['setFillLevel', null],
        'set <fill_level> <pos>' -> 'setFillLevel',

        'dummy_item stackable' -> 'printStackableDummyItem',
        'dummy_item stackable print' -> 'printStackableDummyItem',
        'dummy_item stackable give' -> 'giveStackableDummyItem',
        'dummy_item stackable set item <item>' -> 'setStackableDummyItem',
        'dummy_item stackable set hand' -> 'setStackableDummyItemFromHand',
        'dummy_item stackable reset' -> ['setStackableDummyItem', null],
        'dummy_item unstackable' -> 'printUnstackableDummyItem',
        'dummy_item unstackable print' -> 'printUnstackableDummyItem',
        'dummy_item unstackable give' -> 'giveUnstackableDummyItem',
        'dummy_item unstackable set item <item>' -> 'setUnstackableDummyItem',
        'dummy_item unstackable set hand' -> 'setUnstackableDummyItemFromHand',
        'dummy_item unstackable reset' -> ['setUnstackableDummyItem', null]
    },
    'arguments' -> {
        'fill_level' -> {
            'type' -> 'int',
            'min' -> 0,
            'max' -> 897,
            'suggest' -> []
        }
    },
    'scope' -> 'player'
};

// HELPER FUNCTIONS

_error(error) -> exit(print(format(str('r %s', error))));

_checkVersion(version) -> (
    regex = '(\\d+)\.(\\d+)\.(\\d+)';
    target_version = map(version~regex, number(_));
    scarpet_version = map(system_info('scarpet_version')~regex, number(_));
    return(scarpet_version >= target_version);
);

_getReadingComparators(pos) -> (
    comparators = [];
    map(['north', 'east', 'south', 'west'],
        block1 = block(pos_offset(pos, _, -1));
        if(block1 == 'comparator' && block_state(block1, 'facing') == _, continue(comparators += block1));
        if(solid(block1) && inventory_has_items(block1) == null,
            block2 = block(pos_offset(block1, _, -1));
            if(block2 == 'comparator' && block_state(block2, 'facing') == _, comparators += block2);
        );
    );
    return(comparators);
);

// MAIN

help() -> (
    texts = [
        'fs ' + ' ' * 80, ' \n',
        '#D35400b Fill Level Utilities ', if(_checkVersion('1.4.57'), '@https://github.com/CommandLeo/scarpet/wiki/Fill-Level-Utilities'), 'g by ', '#%color%b CommandLeo', '^g https://github.com/CommandLeo', if(_checkVersion('1.4.57'), '@https://github.com/CommandLeo'), ' \n\n',
        '%color% /%app_name% get [<position>]', 'f ｜', 'g Gets the fill level of the block you are looking at or at the specified position', ' \n',
        '%color% /%app_name% set <fill_level> [<position>]', 'f ｜', 'g Sets the fill level of the block you are looking at or at the specified position', ' \n',
        '%color% /%app_name% dummy_item stackable [get|set|reset|give]', 'f ｜', 'g Gets, sets, resets or gives the stackable dummy item', ' \n',
        '%color% /%app_name% dummy_item unstackable [get|set|reset|give]', 'f ｜', 'g Gets, sets, resets or gives the unstackable dummy item', ' \n',
        'fs ' + ' ' * 80
    ];
    replacement_map = {'%app_name%' -> system_info('app_name'), '%color%' -> global_color};
    print(format(map(texts, reduce(pairs(replacement_map), replace(_a, ..._), _))));
);

// FILL LEVEL

getFillLevel(target) -> (
    fill_level = if(
        target == 'composter' || target == 'cauldron' || target == 'water_cauldron' || target == 'powder_snow_cauldron',
            block_state(pos(target), 'level') || 0,
        inventory_has_items(target) != null,
            if(
                inventory_has_items(target),
                    floor(1 + reduce(inventory_get(target), _a + if(_ , _:1 / stack_limit(_:0), 0), 0) / inventory_size(target) * 14),
                // else
                    0
            )
    );
    return(fill_level);
);

printFillLevel(position) -> (
    target = if(position, block(position), player()~'trace');
    if(!target || target~'type' == 'player', _error('You are not looking at any block'));

    fill_level = getFillLevel(target);
    if(fill_level == null, _error('Invalid block'));
    print(format('f » ', str('gi %s', target), '^g ' + pos(target), 'g  has a fill level of ', str('%s %d', global_color, fill_level)));
);

setFillLevel(fill_level, position) -> (
    target = if(position, block(position), player()~'trace');
    if(!target || target~'type' == 'player', _error('You are not looking at any block'));

    if(
        target == 'composter',
            setComposterFillLevel(target, fill_level),
        target == 'cauldron' || target == 'water_cauldron',
            setCauldronFillLevel(target, fill_level),
        inventory_has_items(target) != null,
            setContainerFillLevel(target, fill_level),
        // else
            _error('Invalid block')
    );

    for(_getReadingComparators(pos(target)), update(_));

    actual_fill_level = getFillLevel(target);
    print(format('f » ', 'g Filled ', str('gi %s', target), '^g ' + pos(target), 'g  with fill level ', str('%s %d', global_color, actual_fill_level), if(actual_fill_level != fill_level, str('g  (instead of %s)', fill_level)))),
);

setComposterFillLevel(composter, fill_level) -> (
    if(fill_level > 8 || fill_level < 0, _error('Invalid fill level'));
    set(composter, 'composter', {'level' -> fill_level});
);

setCauldronFillLevel(cauldron, fill_level) -> (
    if(fill_level > 3 || fill_level < 0, _error('Invalid fill level'));
    if(fill_level == 0,
        set(cauldron, 'cauldron'),
        set(cauldron, if(system_info('game_major_target') >= 17, 'water_cauldron', 'cauldron'), {'level' -> fill_level})
    );
);

setContainerFillLevel(block, fill_level) -> (
    slots = inventory_size(block);
    items = if(fill_level == 1, 1, ceil(slots * 64 / 14 * (fill_level - 1)));
    if(fill_level <= 15,
        loop(slots,
            amount = min(items, 64);
            items += -amount;
            inventory_set(block, _, amount, ...global_stackable_dummy_item);
        ),
        loop(slots,
            amount = min(64, items / 64);
            items += -if(amount < 1, amount, floor(amount)) * 64;
            if(amount < 1,
                inventory_set(block, _, amount * 64, ...global_stackable_dummy_item),
                inventory_set(block, _, if(_ == slots - 1, ceil(amount), floor(amount)), ...global_unstackable_dummy_item)
            );
        )
    );
);

// DUMMY ITEM

printStackableDummyItem() -> (
    [item, nbt] = global_stackable_dummy_item;
    print(format('f » ', 'g The current stackable dummy item is ', if(nbt, ...[str('%s %s*', global_color, item), str('^g %s', nbt)], str('%s %s', global_color, item)))),
);

setStackableDummyItemFromHand() -> (
    holds = player()~'holds';
    if(!holds, _error('You are not holding any item'));

    setStackableDummyItem(holds);
);

setStackableDummyItem(item_tuple) -> (
    if(
        !item_tuple,
            global_stackable_dummy_item = global_default_stackable_dummy_item;
            print(format('f » ', 'g The stackable dummy item has been reset')),
        // else
            [item, count, nbt] = item_tuple;
            if(stack_limit(item) == 1, _error('You can use only stackable items'));
            global_stackable_dummy_item = [item, nbt];
            print(format('f » ', 'g The stackable dummy item has been set to ', str('%s %s', global_color, item), if(nbt, str('^g %s', nbt)))),
    );
);

giveStackableDummyItem() -> (
    [item, nbt] = global_stackable_dummy_item;
    run(str('give %s %s%s', player()~'command_name', item, nbt || ''));
);

printUnstackableDummyItem() -> (
    [item, nbt] = global_unstackable_dummy_item;
    print(format('f » ', 'g The current unstackable dummy item is ', if(nbt, ...[str('%s %s*', global_color, item), str('^g %s', nbt)], str('%s %s', global_color, item)))),
);

setUnstackableDummyItemFromHand() -> (
    holds = player()~'holds';
    if(!holds, _error('You are not holding any item'));

    setUnstackableDummyItem(holds);
);

setUnstackableDummyItem(item_tuple) -> (
    if(
        !item_tuple,
            global_unstackable_dummy_item = global_default_unstackable_dummy_item;
            print(format('f » ', 'g The unstackable dummy item has been reset')),
        // else
            [item, count, nbt] = item_tuple;
            if(stack_limit(item) != 1, _error('You can use only unstackable items'));
            global_unstackable_dummy_item = [item, nbt];
            print(format('f » ', 'g The unstackable dummy item has been set to ', str('%s %s', global_color, item), if(nbt, str('^g %s', nbt)))),
    );
);

giveUnstackableDummyItem() -> (
    [item, nbt] = global_unstackable_dummy_item;
    run(str('give %s %s%s', player()~'command_name', item, nbt || ''));
);
