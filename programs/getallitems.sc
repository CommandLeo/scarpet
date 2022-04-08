// Get All Items by CommandLeo

global_survival_unobtainables = ['bedrock', 'end_portal_frame', 'barrier', 'light', 'command_block', 'repeating_command_block', 'chain_command_block', 'structure_void', 'structure_block', 'jigsaw', 'sculk_sensor', 'petrified_oak_slab', 'spawner', 'player_head', 'budding_amethyst', 'chorus_plant', 'dirt_path', 'grass_path', 'farmland', 'infested_stone', 'infested_cobblestone', 'infested_stone_bricks', 'infested_mossy_stone_bricks', 'infested_cracked_stone_bricks', 'infested_chiseled_stone_bricks', 'infested_deepslate', 'command_block_minecart', 'knowledge_book', 'debug_stick', 'bundle', 'axolotl_spawn_egg', 'bat_spawn_egg', 'bee_spawn_egg', 'blaze_spawn_egg', 'cat_spawn_egg', 'cave_spider_spawn_egg', 'chicken_spawn_egg', 'cod_spawn_egg', 'cow_spawn_egg', 'creeper_spawn_egg', 'dolphin_spawn_egg', 'donkey_spawn_egg', 'drowned_spawn_egg', 'elder_guardian_spawn_egg', 'enderman_spawn_egg', 'endermite_spawn_egg', 'evoker_spawn_egg', 'fox_spawn_egg', 'ghast_spawn_egg', 'glow_squid_spawn_egg', 'goat_spawn_egg', 'guardian_spawn_egg', 'hoglin_spawn_egg', 'horse_spawn_egg', 'husk_spawn_egg', 'llama_spawn_egg', 'magma_cube_spawn_egg', 'mooshroom_spawn_egg', 'mule_spawn_egg', 'ocelot_spawn_egg', 'panda_spawn_egg', 'parrot_spawn_egg', 'phantom_spawn_egg', 'pig_spawn_egg', 'piglin_spawn_egg', 'piglin_brute_spawn_egg', 'pillager_spawn_egg', 'polar_bear_spawn_egg', 'pufferfish_spawn_egg', 'rabbit_spawn_egg', 'ravager_spawn_egg', 'salmon_spawn_egg', 'sheep_spawn_egg', 'shulker_spawn_egg', 'silverfish_spawn_egg', 'skeleton_spawn_egg', 'skeleton_horse_spawn_egg', 'slime_spawn_egg', 'spider_spawn_egg', 'squid_spawn_egg', 'stray_spawn_egg', 'strider_spawn_egg', 'trader_llama_spawn_egg', 'tropical_fish_spawn_egg', 'turtle_spawn_egg', 'vex_spawn_egg', 'villager_spawn_egg', 'vindicator_spawn_egg', 'wandering_trader_spawn_egg', 'witch_spawn_egg', 'wither_skeleton_spawn_egg', 'wolf_spawn_egg', 'zoglin_spawn_egg', 'zombie_spawn_egg', 'zombie_horse_spawn_egg', 'zombie_villager_spawn_egg', 'zombified_piglin_spawn_egg'];
global_junk = ['filled_map', 'written_book', 'tipped_arrow'];
if(system_info('game_major_target') < 18, global_survival_unobtainables += 'spore_blossom');

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

getItems(category, stackability) -> (
    item_list = filter(item_list(), _ != 'air' && if(category == 'survival_obtainables', global_survival_unobtainables~_ == null && global_junk~_ == null, true) && global_stackabilities:stackability~stack_limit(_) != null);
    item_amount = length(item_list);
    screen = create_screen(player(), 'generic_9x6', '');
    loop(ceil(item_amount / 27), 
        items = slice(item_list, _ * 27, min(item_amount, (_ + 1) * 27));
        box_content = map(items, {'id' -> _, 'Count' -> 1, 'Slot' -> _i});
        inventory_set(screen, _, 1, 'white_shulker_box', {'BlockEntityTag' -> {'Items' -> box_content}});
    );
);