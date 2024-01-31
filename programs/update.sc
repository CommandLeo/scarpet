// Block Updater by CommandLeo

__config() -> {
    'commands' -> {
        '' -> ['updateBlocks', null, null, null],
        '<start> <end>' -> ['updateBlocks', null],
        '<start> <end> <block>' -> 'updateBlocks'
    },
    'arguments' -> {
        'start' -> {
            'type' -> 'pos'
        },
        'end' -> {
            'type' -> 'pos'
        }
    },
    'scope' -> 'player'
};

updateBlocks(start, end, block) -> (
    trace = player()~'trace';
    if(!start || !end,
        if(
            trace && type(trace) == 'block',
                update(trace);
                print(format('f » ', 'g Updated ', str('gi %s', trace), str('g  at [%d, %d, %d]', ...pos(trace)))),
            // else
                print(format('r You are not looking at any block'))
        );
        exit();
    );
    updated_count = 0;
    volume(start, end,
        if(!air(_) && (!block || _ == block),
            update(_);
            updated_count += 1;
        );
    );
    print(format('f » ', 'g Updated ', str('d %d', updated_count),  str('g  block%s', if(updated_count != 1, 's', ''))));
)