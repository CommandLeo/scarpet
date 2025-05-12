// Get All Items by CommandLeo

global_color = '#E84393';
global_items = item_list();
global_sort_alphabetically = false;

global_obtainabilities = {'everything' -> 'Everything', 'main_storage' -> 'Main Storage', 'survival_obtainables' -> 'Survival Obtainables'};
global_stackabilities = {'stackables' -> [16, 64], '64_stackables' -> [64], '16_stackables' -> [16], 'unstackables' -> [1]};

global_survival_unobtainable_items = [
    'bedrock',
    'budding_amethyst',
    'petrified_oak_slab',
    'chorus_plant',
    'spawner', // for 1.19.2-
    'monster_spawner', // for 1.19.3+
    'farmland',
    ...filter(global_items, _~'infested' != null),
    'reinforced_deepslate',
    'end_portal_frame',
    'command_block',
    'barrier',
    'light',
    'grass_path', // for 1.16-
    'dirt_path', // for 1.17+
    'repeating_command_block',
    'chain_command_block',
    'structure_void',
    'structure_block',
    'jigsaw',
    'bundle',
    ...filter(global_items, _~'spawn_egg' != null),
    'player_head',
    'command_block_minecart',
    'knowledge_book',
    'debug_stick',
    'frogspawn',
    'trial_spawner',
    'vault',
    'test_block',
    'test_instance_block'
];
if(system_info('game_data_version') < 2825, global_survival_unobtainable_items += 'spore_blossom'); // 1.18 Experimental 1
if(system_info('game_data_version') < 3066, global_survival_unobtainable_items += 'sculk_sensor'); // Deep Dark Experimental Snapshot 1
global_junk_items = ['filled_map', 'written_book', 'tipped_arrow', 'firework_star', 'firework_rocket'];
system_variable_set('survival_unobtainable_items', global_survival_unobtainable_items);
system_variable_set('junk_items', global_junk_items);

_checkVersion(version) -> (
    regex = '(\\d+)\.(\\d+)\.(\\d+)';
    target_version = map(version~regex, number(_));
    scarpet_version = map(system_info('scarpet_version')~regex, number(_));
    return(scarpet_version >= target_version);
);

__config() -> {
    'commands' -> {
        '' -> 'menu',
        'chests' -> ['giveChests', 'main_storage', 'stackables'],
        'chests <obtainability>' -> ['giveChests', null],
        'chests <obtainability> <stackability>' -> 'giveChests',

        'screen' -> ['showScreen', 'main_storage', 'stackables'],
        'screen <obtainability>' -> ['showScreen', null],
        'screen <obtainability> <stackability>' -> 'showScreen'
    },
    'arguments' -> {
        'obtainability' -> {
            'type' -> 'term',
            'options' -> keys(global_obtainabilities)
        },
        'stackability' -> {
            'type' -> 'term',
            'options' -> keys(global_stackabilities)
        }
    },
    'requires' -> {
        'carpet' -> '>=1.4.57'
    },
    'scope' -> 'player'
};

menu() -> (
    texts = [
       'fs ' + ' ' * 80, ' \n',
        str('%sb Get All Items', global_color), if(_checkVersion('1.4.57'), ...['@https://github.com/CommandLeo/scarpet/wiki/Get-All-Items', '^g Click to visit the wiki']), 'g  by ', str('%sb CommandLeo', global_color), '^g https://github.com/CommandLeo', if(_checkVersion('1.4.57'), '@https://github.com/CommandLeo'),' \n\n',
        'g A script to get all items in the game inside shulker boxes.', '  \n',
        'g Run ', str('%s /%s screen', global_color, system_info('app_name')), str('!/%s screen', system_info('app_name')), '^g Click to run the command', 'g  to view the shulker boxes in a screen or ', str('%s /%s chests', global_color, system_info('app_name')), str('!/%s chests', system_info('app_name')), '^g Click to run the command', 'g  to get them in chests.', '  \n',
        'fs ' + ' ' * 80
    ];
    print(format(texts));
);

getItems(category, stackability) -> (
    items = map(global_items, [_, null]);
    firework_rockets = map(range(3), ['firework_rocket', if(system_info('game_pack_version') >= 33, {'fireworks' -> {'flight_duration' -> _ + 1}}, {'Fireworks' -> {'Flight' -> _ + 1}})]);
    if(category == 'main_storage', put(items, items~['firework_rocket', null], firework_rockets, 'extend'));
    items = filter(items,
        [item, nbt] = _;
        category_check = if(
            category == 'main_storage',
                global_survival_unobtainable_items~item == null && (global_junk_items~item == null || nbt != null) && item~'shulker_box' == null,
            category == 'survival_obtainables',
                global_survival_unobtainable_items~item == null,
            true
        );
        stackability_check = !stackability || global_stackabilities:stackability~stack_limit(item) != null;
        item != 'air' && category_check && stackability_check;
    );
    if(!_checkVersion('1.4.113') || global_sort_alphabetically, items = sort_key(items, _:0));
    return(items);
);

giveChests(category, stackability) -> (
    items = getItems(category, stackability);
    shulker_boxes = map(range(ceil(length(items) / 27)), map(slice(items, _ * 27, min(length(items), (_ + 1) * 27)), [item, nbt] = _; if(system_info('game_pack_version') >= 33, {'slot' -> _i, 'item' -> {'id' -> item, 'components' -> nbt || {}}}, {'Slot' -> _i, 'id' -> item, 'Count' -> 1, ...if(nbt, {'tag' -> nbt}, {})})));
    loop(ceil(length(items) / 27 / 27),
        chest_contents = map(slice(shulker_boxes, _ * 27, min(ceil(length(items) / 27), (_ + 1) * 27)), if(system_info('game_pack_version') >= 33, {'slot' -> _i, 'item' -> {'id' -> 'white_shulker_box', 'components' -> {'container' -> _}}}, {'Slot' -> _i, 'id' -> 'white_shulker_box', 'Count' -> 1, 'tag' -> {'BlockEntityTag' -> {'Items' -> _}}}));
        run('/give @s chest' + if(system_info('game_pack_version') >= 33, str('[container=%s]', encode_nbt(chest_contents)), encode_nbt({'BlockEntityTag' -> {'Items' -> chest_contents}})));
    );
);

showScreen(category, stackability) -> (
    items = getItems(category, stackability);
    screen = create_screen(player(), 'generic_9x6', str('%s | %d items', global_obtainabilities:category, length(items)));
    loop(ceil(length(items) / 27), 
        shulker_box_items = slice(items, _ * 27, min(length(items), (_ + 1) * 27));
        shulker_box_contents = map(shulker_box_items, [item, nbt] = _; if(system_info('game_pack_version') >= 33, {'slot' -> _i, 'item' -> {'id' -> item, 'components' -> nbt || {}}}, {'Slot' -> _i, 'id' -> item, 'Count' -> 1, ...if(nbt, {'tag' -> nbt}, {})}));
        inventory_set(screen, _, 1, 'white_shulker_box', if(system_info('game_pack_version') >= 33, {'components' -> {'container' -> shulker_box_contents}, 'id' -> 'white_shulker_box'}, {'BlockEntityTag' -> {'Items' -> shulker_box_contents}}));
    );
);