// Quick Killing by CommandLeo

__config() -> {
    'commands' -> {
        '' -> ['kill', 0],
        '<distance>' -> 'kill'
    },
    'arguments' -> {
        'distance' -> {
            'type' -> 'int',
            'suggest' -> []
        }
    },
    'requires' -> {
        'carpet' -> '>=1.4.57'
    },
    'scope' -> 'global'
};

kill(distance) -> (
    killed_count = 0
    for(['item', 'experience_orb'],
        entities = if(distance > 0, entity_area(_, system_info('source_position'), [distance, distance, distance]), entity_list(_));
        for(entities,
            modify(_, 'kill');
            killed_count += 1;
        );
    );
    print(format('f » ', 'g Killed ', str('d %d', killed_count),  str('g  entit%s', if(killed_count != 1, 'ies', 'y'))));
);