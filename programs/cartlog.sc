__config() -> {
    'commands' -> {
        '' -> 'toggle_visibility',
        'pos' -> ['toggle_setting', 'pos'],
        'speed' -> ['toggle_setting', 'speed'],
        'fall_distance' -> ['toggle_setting', 'fall_distance'],
        'fill_level' -> ['toggle_setting', 'fill_level'],
        'locked' -> ['toggle_setting', 'locked'],
        'fuse' -> ['toggle_setting', 'fuse'],
        'hitbox' -> ['toggle_setting', 'hitbox'],
    },
    'scope' -> 'player'
};

toggle_visibility() -> global_on = !global_on;

toggle_setting(setting) -> global_settings:setting = !global_settings:setting;

__on_start() -> (
    global_settings = {
        'pos' -> true,
        'speed' -> true,
        'fall_distance' -> false,
        'fill_level' -> true,
        'locked' -> true,
        'fuse' -> true,
        'hitbox' -> true
    };
);

__on_tick() -> (
    if(!global_on, return());
    for(entity_list('minecarts'),
        i = 0;
        cart = _;

        if(global_settings:'fuse' && cart~'type' == 'tnt_minecart',
            fuse = query(cart, 'nbt', 'TNTFuse');
            draw_shape('label', 1, {'player' -> player(), 'pos' -> [0, 1, 0], 'follow' -> _, 'height' -> i+=1, 'text' -> '', 'value' -> str('Fuse: %d', fuse)})
        );
        if(global_settings:'locked' && cart~'type' == 'hopper_minecart',
            locked = !query(cart, 'nbt', 'Enabled');
            draw_shape('label', 1, {'player' -> player(), 'pos' -> [0, 1, 0], 'follow' -> _, 'height' -> i+=1, 'text' -> '', 'value' -> str('Locked: %b', locked)})
        );
        if(global_settings:'fill_level' && inventory_has_items(cart) != null,
            fill_level = if(inventory_has_items(cart), floor(1 + reduce(inventory_get(cart), _a + if(_ , _:1 / stack_limit(_:0), 0), 0) / inventory_size(cart) * 14), 0);
            draw_shape('label', 1, {'player' -> player(), 'pos' -> [0, 1, 0], 'follow' -> _, 'height' -> i+=1, 'text' -> '', 'value' -> str('Fill Level: %d', fill_level)})
        );

        if(global_settings:'fall_distance',
            fall_distance = query(cart, 'nbt', 'FallDistance');
            draw_shape('label', 1, {'player' -> player(), 'pos' -> [0, 1, 0], 'follow' -> _, 'height' -> i+=1, 'text' -> '', 'value' -> str('Fall Distance: %.2f', fall_distance)})
        );
        if(global_settings:'speed',
            speed_x = max(-8, min(8, cart~'motion_z' * 20 + 1e-10));
            speed_y = cart~'motion_y' * 20 + 1e-10;
            speed_z = max(-8, min(8, cart~'motion_x' * 20 + 1e-10));
            draw_shape('label', 1, {'player' -> player(), 'pos' -> [0, 1, 0], 'follow' -> _, 'height' -> i+=1, 'text' -> '', 'value' -> str('Speed Z: %.2f bps', speed_x)});
            draw_shape('label', 1, {'player' -> player(), 'pos' -> [0, 1, 0], 'follow' -> _, 'height' -> i+=1, 'text' -> '', 'value' -> str('Speed Y: %.2f bps', speed_y)});
            draw_shape('label', 1, {'player' -> player(), 'pos' -> [0, 1, 0], 'follow' -> _, 'height' -> i+=1, 'text' -> '', 'value' -> str('Speed X: %.2f bps', speed_z)});
        );
        if(global_settings:'pos',
            x = cart~'x';
            y = cart~'y';
            z = cart~'z';
            draw_shape('label', 1, {'player' -> player(), 'pos' -> [0, 1, 0], 'follow' -> _, 'height' -> i+=1, 'text' -> '', 'value' -> str('Z: %.2f', z)});
            draw_shape('label', 1, {'player' -> player(), 'pos' -> [0, 1, 0], 'follow' -> _, 'height' -> i+=1, 'text' -> '', 'value' -> str('Y: %.2f', y)});
            draw_shape('label', 1, {'player' -> player(), 'pos' -> [0, 1, 0], 'follow' -> _, 'height' -> i+=1, 'text' -> '', 'value' -> str('X: %.2f', x)});
        );

        if(global_settings:'hitbox', draw_shape('box', 1, {'player' -> player(), 'from' -> pos(cart) - [0.98/2, 0, 0.98/2], 'to' -> pos(cart) + [0.98/2, 0.7, 0.98/2], 'fill' -> 0x9b59b688}));
    );
)