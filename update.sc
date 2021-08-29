// Block Updater by CommandLeo

__config() -> {
    'commands' -> {
        '' -> ['updateBlock', null, null, null],
        '<start>' -> ['updateBlock', null, null],
        '<start> <end>' -> ['updateBlock', null],
        '<start> <end> <block>' -> 'updateBlock'
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

updateBlock(start, end, block) -> {
    if(!start, return(update(query(player(), 'trace', 5, 'blocks'))));
    if(!end, return(update(start)));
    volume(start, end, if(!block || _ == block, update(_)));
}