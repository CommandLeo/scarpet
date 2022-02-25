// Comparator Signal Strength Display by CommandLeo

__config() -> {
    'commands' -> {
        '' -> 'toggle'
    },
    'scope'-> 'player'
};

toggle() -> (
    print(player(), format('f »', 'g Comparator Signal Strength Display ', if(global_enabled = !global_enabled, 'l enabled', 'r disabled')));
);

displayComparatorSignalStrength() -> (
    if(global_enabled, 
        trace = player()~'trace';
        if(trace == 'comparator', draw_shape('label', 2, {'pos' -> pos(trace) + 0.5, 'text' -> block_data(trace):'OutputSignal', 'player'-> player(), 'color' -> 0xff4757ff}));
    );
    schedule(1, 'displayComparatorSignalStrength');
);

__on_start() -> displayComparatorSignalStrength();