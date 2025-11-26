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
        },
        'block' -> {
            'type' -> 'blockpredicate'
        }
    },
    'scope' -> 'player'
};

// HELPER FUNCTIONS

_error(error) -> (
    print(format(str('r %s', error)));
    run('playsound block.note_block.didgeridoo master @s');
    exit();
);

// UTILITY FUNCTIONS

_checkPredicate(block, block_predicate) -> (
    [predicate_block, predicate_block_tag, predicate_block_states, predicate_block_data] = block_predicate;
    block_states = block_state(block);
    block_data = block_data(block);
    return(
        (!predicate_block || block == predicate_block) &&
        (!predicate_block_tag || block_tags(block, predicate_block_tag)) &&
        all(predicate_block_states, block_states:_ == predicate_block_states:_) &&
        (!predicate_block_data || all(parse_nbt(predicate_block_data), block_data:_ == predicate_block_data:_))
    );
);

// MAIN

updateBlocks(from_pos, to_pos, block_predicate) -> (
    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error('You are not looking at any block'));
        from_pos = trace;
    );
    if(!to_pos, to_pos = from_pos);

    last_block = null;
    affected_blocks = volume(from_pos, to_pos,
        block = _;

        if(!air(block) && (!block_predicate || _checkPredicate(block, block_predicate)),
            update(block);
            updated_count += 1;
            last_block = block;
        );
    );

    if(
        affected_blocks == 0,
            _error('No block was found'),
        affected_blocks == 1,
            print(format('f » ', 'g Updated ', str('gi %s', last_block), 'g  at ' + pos(last_block))),
        // else
            print(format('f » ', 'g Updated ', str('d %s', affected_blocks), str('g  block%s', if(affected_blocks == 1, '', 's'))));
    );
);