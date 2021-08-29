// Quick Killing by CommandLeo

__config() -> {
    'commands' -> {
        '' -> ['kill', 0],
        '<distance>' -> 'kill'
    },
    'arguments' -> {
        'distance' -> {
            'type' -> 'int',
            'suggest' -> [100]
        }
    },
    'scope' -> 'player'
};

kill(distance)->(
    for(['item', 'item_frame'], for(if(distance > 0, entity_area(_, pos(player()), [distance, distance, distance]), entity_list(_)), modify(_, 'kill'); killed_count += 1));
    print(format('f » ', 'g Killed ', str('d %d', killed_count),  str('g  entit%s', if(killed_count != 1, 'ies', 'y'))));
);