// Get All Items by CommandLeo

game_version = system_info('game_major_target');
global_obtainabilities = ['everything', 'main_storage', 'survival_obtainables'];
global_stackabilities = {'stackables' -> [16, 64], 'unstackables' -> [1], '64_stackables' -> [64], '16_stackables' -> [16]};

global_items = map(item_list(), [_, null]);
global_junk_items = ['filled_map', 'written_book', 'tipped_arrow', 'firework_star', 'firework_rocket', 'bee_nest'];

__config() -> {
    'commands' -> {
        '' -> ['getAllItems', 'main_storage', 'stackables'],
        '<obtainability>' -> ['getAllItems', ''],
        '<obtainability> <stackability>' -> 'getAllItems'
    },
    'arguments' -> {
        'obtainability' -> {
            'type' -> 'term',
            'options' -> global_obtainabilities
        },
        'stackability' -> {
            'type' -> 'term',
            'options' -> keys(global_stackabilities)
        }
    },
    'libraries' -> [
        {
            'source' -> 'https://raw.githubusercontent.com/CommandLeo/scarpet/main/libraries/survival_unobtainables.scl'
        }
    ],
    'requires' -> {
        'carpet' -> '>=1.4.57'
    },
    'scope' -> 'player'
};

import('survival_unobtainables', 'global_survival_unobtainables');

getAllItems(category, stackability) -> (
    items = if(category == 'main_storage', [...global_items, ...map(range(3), ['firework_rocket', {'Fireworks' -> {'Flight' -> _ + 1}}])], global_items);
    items = sort_key(filter(items,
        [item, nbt] = _;
        category_check = if(
            category == 'main_storage',
                global_survival_unobtainables~item == null && (global_junk_items~item == null || nbt != null) && item~'shulker_box' == null,
            category == 'survival_obtainables',
                global_survival_unobtainables~item == null,
            true
        );
        stackability_check = !stackability || global_stackabilities:stackability~stack_limit(item) != null;
        item != 'air' && category_check && stackability_check;
    ), _:0);

    screen = create_screen(player(), 'generic_9x6', '');
    loop(ceil(length(items) / 27), 
        shulker_box_items = slice(items, _ * 27, min(length(items), (_ + 1) * 27));
        shulker_box_content = map(shulker_box_items, [item, nbt] = _; {'Slot' -> _i, 'id' -> item, 'Count' -> 1, ...if(nbt, {'tag' -> nbt}, {})});
        inventory_set(screen, _, 1, 'white_shulker_box', {'BlockEntityTag' -> {'Items' -> shulker_box_content}});
    );
);
