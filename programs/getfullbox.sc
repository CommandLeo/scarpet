// Full Shulker Box Generator by CommandLeo

global_colors = ['white', 'orange', 'magenta', 'light_blue', 'yellow', 'lime', 'pink', 'gray', 'light_gray', 'cyan', 'purple', 'blue', 'brown', 'green', 'red', 'black'];

__config() -> {
    'commands' -> {
        '<item>' -> ['getFullBox', null],
        '<item> <box_color>' -> 'getFullBox'
    },
    'arguments' -> {
        'item' -> {
            'type' -> 'item'
        },
        'box_color' -> {
            'type' -> 'term',
            'options' -> global_colors
        }
    }
};

getFullBox(item_tuple, box_color) -> (
    slot = inventory_find(player(), 'air');
    if(slot < 0 || slot >= 36, exit(print(format('r No space left in your inventory'))));
    [item, count, nbt] = item_tuple;
    shulker_box = if(box_color, str('%s_shulker_box', box_color), 'shulker_box');
    box_content = map(range(27), if(system_info('game_pack_version') >= 33, {'slot' -> _, 'item' -> {'id' -> item, 'count' -> stack_limit(item), 'components' -> nbt:'components' || {}}}, {'id' -> item, 'Count' -> stack_limit(item), 'Slot' -> _, 'tag' -> nbt}));
    inventory_set(player(), slot, 1, shulker_box, if(system_info('game_pack_version') >= 33, {'components' -> {'container' -> box_content}, 'id' -> shulker_box}, {'BlockEntityTag' -> {'Items' -> box_content}}));
);
