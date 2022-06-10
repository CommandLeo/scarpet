// Get All Items by CommandLeo

global_survival_unobtainables = ['bedrock', 'end_portal_frame', 'barrier', 'light', 'command_block', 'repeating_command_block', 'chain_command_block', 'structure_void', 'structure_block', 'jigsaw', 'sculk_sensor', 'petrified_oak_slab', 'spawner', 'player_head', 'budding_amethyst', 'chorus_plant', 'dirt_path', 'grass_path', 'farmland', 'frogspawn', 'infested_stone', 'infested_cobblestone', 'infested_stone_bricks', 'infested_mossy_stone_bricks', 'infested_cracked_stone_bricks', 'infested_chiseled_stone_bricks', 'infested_deepslate', 'reinforced_deepslate', 'command_block_minecart', 'knowledge_book', 'debug_stick', 'bundle'];
global_junk = ['filled_map', 'written_book', 'tipped_arrow'];

version = system_info('game_major_target');
if(version < 18, global_survival_unobtainables += 'spore_blossom');
if(version < 19, global_survival_unobtainables += 'sculk_sensor');

global_categories = ['all', 'survival_obtainables'];
global_stackabilities = {'all' -> [1, 16, 64], 'stackables' -> [16, 64], 'unstackables' -> [1], '64_stackables' -> [64], '16_stackables' -> [16]};

__config() -> {
    'commands' -> {
        '' -> ['getItems', 'survival_obtainables', 'stackables'],
        '<category>' -> ['getItems', 'all'],
        '<category> <stackability>' -> 'getItems'
    },
    'arguments' -> {
        'category' -> {
            'type' -> 'term',
            'suggest' -> global_categories,
            'options' -> global_categories
        },
        'stackability' -> {
            'type' -> 'term',
            'suggest' -> keys(global_stackabilities),
            'options' -> keys(global_stackabilities)
        }
    },
    'requires' -> {
        'carpet' -> '>=1.4.57'
    },
    'scope' -> 'player'
};

_isSurvivalUnobtainable(item) -> item~'spawn_egg' != null || item~'spawn_egg' != null || global_survival_unobtainables~item != null;

getItems(category, stackability) -> (
    item_list = filter(item_list(), _ != 'air' && if(category == 'survival_obtainables', !_isSurvivalUnobtainable(_) && global_junk~_ == null, true) && global_stackabilities:stackability~stack_limit(_) != null);
    item_amount = length(item_list);
    screen = create_screen(player(), 'generic_9x6', '');
    loop(ceil(item_amount / 27), 
        items = slice(item_list, _ * 27, min(item_amount, (_ + 1) * 27));
        box_content = map(items, {'id' -> _, 'Count' -> 1, 'Slot' -> _i});
        inventory_set(screen, _, 1, 'white_shulker_box', {'BlockEntityTag' -> {'Items' -> box_content}});
    );
);
