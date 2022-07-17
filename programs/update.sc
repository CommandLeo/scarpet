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

updateBlocks(start, end, block) -> {
    trace = player()~'trace';
    if(!start && trace && type(trace) == 'block', return(update(trace)));
    volume(start, end, if(!block || _ == block, update(_)));
}