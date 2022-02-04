// Fill Level Utilities by CommandLeo

__config() -> {
    'commands' -> {
        '' -> 'help',
        'get' -> ['getFillLevel', null],
        'get <pos>' -> 'getFillLevel',
        'set <filllevel>' -> ['setFillLevel', null],
        'set <filllevel> <pos>' -> 'setFillLevel'
    },
    'arguments' -> {
        'filllevel' -> {
            'type' -> 'int',
            'suggest' -> [],
            'min' -> 0,
            'max' -> 897
        }
    },
    'scope' -> 'player'
};

help() -> (
    print(player(), format(
        'fs -----------------------------------------------------', ' \n',
        '#D35400b Fill Level Utilities ', '#E67E22 by ', '#E67E22b CommandLeo', ' \n\n',
        '#E67E22 /fill_level get [<pos>] ', 'f ｜ ', 'g Gets the fill level of a container', ' \n',
        '#E67E22 /fill_level set <fill_level> [<pos>] ', 'f ｜ ', 'g Sets the fill level of a container', ' \n',
        'fs -----------------------------------------------------'
    ));
);

getFillLevel(position) -> (
    b = position || player()~'trace';
    if(!b || type(b) == 'entity' || inventory_has_items(b) == null, exit(print('§cThat block is not a container!')));
    b = block(b);
    ss = if(inventory_has_items(b), floor(1 + reduce(inventory_get(b), _a + if(_ , _:1 / stack_limit(_:0), 0), 0) / inventory_size(b) * 14), 0);
    print(format('f » ', str('g %s has a fill level of ', b), str('#E67E22 %d', ss)));
);

setFillLevel(ss, position) -> (
    b = position || player()~'trace';
    if(!b || type(b) == 'entity' || inventory_has_items(b) == null, exit(print('§cThat block is not a container!')));
    b = block(b);
    slots = inventory_size(b);
    amount = if(ss == 1, 1, ceil(slots / 14 * (ss - 1)));
    stacks = ceil(amount / 64);
    if(ss <= 15,
        loop(slots, inventory_set(b, _, if(_ < amount, 1, 0), 'cake')),
        loop(slots, inventory_set(b, _, amount - 64 * _, 'shulker_box'))
    );
    print(format('f » ', str('g Filled %s with fill level ', b), str('#E67E22 %d', ss)));
);
