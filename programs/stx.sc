// StorageTechX by CommandLeo

global_color = '#00D2D3';
global_modules = read_file('stx_modules', 'json');
global_directions = ['down', 'up', 'north', 'south', 'west', 'east'];
global_cardinal_directions = ['north', 'east', 'south', 'west'];
global_containers = [...filter(item_list(), _~'shulker_box' != null), 'chest', 'double_chest', 'barrel', 'hopper', 'dropper', 'dispenser'];
global_container_modes = {
    'full' -> 'Full',
    'full_boxes' -> 'Full Boxes',
    'prefill' -> 'Prefill',
    'prefill_unstackables' -> 'Prefill Unstackables',
    'single_item' -> 'Single Item',
    'single_stack' -> 'Single Stack'
};
global_chest_modes = {
    'full_boxes' -> 'Full Boxes',
    'stacks' -> 'Item Stacks',
    'single_items' -> 'Single Items'
};
global_item_filters = {
    'ssi_ss2' -> 'SSI SS2',
    'ss3' -> 'SS3',
    'overstacking' -> 'Overstacking',
    'box_sorter' -> 'Box Sorter',
    'stack_separation' -> 'Stack Separation'
};
global_shulker_box_colors = {
    'default' -> '#956794',
    'white' -> '#F9FFFF',
    'orange' -> '#F9801D',
    'magenta' -> '#C64FBD',
    'light_blue' -> '#3AB3DA',
    'yellow' -> '#FFD83D',
    'lime' -> '#80C71F',
    'pink' -> '#F38CAA',
    'gray' -> '#474F52',
    'light_gray' -> '#9C9D97',
    'cyan' -> '#169C9D',
    'purple' -> '#8932B7',
    'blue' -> '#3C44A9',
    'brown' -> '#825432',
    'green' -> '#5D7C15',
    'red' -> '#B02E26',
    'black' -> '#1D1C21'
};
global_item_frame_types = {
    'item_frame' -> 'Item Frame',
    'glow_item_frame' -> 'Glow Item Frame'
};
global_extra_container_mode_options = {
    'repeat' -> 'm',
    'no_fill' -> 'f',
    'dummy_item' -> 'q',
    'air' -> 'w'
};
global_invalid_items_mode_options = {
    'error' -> 'n',
    'skip' -> 'r',
    'no_fill' -> 'f',   
    'dummy_item' -> 'q',
    'air' -> 'w'
};
global_air_mode_options = {
    'default' -> 'w',
    'dummy_item' -> 'q',
    'invalid' -> 'n'
};
global_double_chest_direction_options = {
    'left',
    'right'
};
global_default_stackable_dummy_item = ['structure_void', '{display:{Name:\'"╚═Dummy═╝"\'}}'];
global_default_unstackable_dummy_item = ['shears', null];

// Load the config
config = read_file('config', 'json');
global_stackable_dummy_item = config:'stackable_dummy_item' || global_default_stackable_dummy_item;
global_unstackable_dummy_item = config:'unstackable_dummy_item' || global_default_unstackable_dummy_item;
global_shulker_box_color = config:'shulker_box_color' || 'default';
global_item_frame_type = config:'item_frame_type' || 'item_frame';
global_filter_item_amount = config:'filter_item_amount' || 2;
global_double_chest_direction = config:'double_chest_direction' || 'right'; // left | right
global_extra_containers_mode = config:'extra_containers_mode' || 'no_fill'; // repeat | no_fill | dummy_item | air
global_invalid_items_mode = config:'invalid_items_mode' || 'error'; // error | skip | no_fill | dummy_item | air
global_air_mode = config:'air_mode' || 'default'; // default | dummy_item | invalid
global_placing_offset = config:'placing_offset' || 1;
global_skip_empty_spots = config:'skip_empty_spots' || false;
global_replace_blocks = config:'replace_blocks' || false;
global_minimal_filling = config:'minimal_filling' || false;

_checkVersion(version) -> (
    regex = '(\\d+)\.(\\d+)\.(\\d+)';
    target_version = map(version~regex, number(_));
    scarpet_version = map(system_info('scarpet_version')~regex, number(_));
    return(scarpet_version >= target_version);
);

__config() -> {
    'commands' -> {
        '' -> 'menu',
        'help' -> 'help',

        ...if(global_modules, {
            'modules' -> 'listModules',
            'modules list' -> 'listModules',
            'modules install <module>' -> 'installModule',
        }, {}),

        'item_list create <name> items <items>' -> 'createItemListFromItems',
        'item_list create <name> item_layout <item_layout>' -> 'createItemListFromItemLayout',
        'item_list create <name> container' -> ['createItemListFromContainer', null],
        'item_list create <name> container <pos>' -> 'createItemListFromContainer',
        'item_list create <name> area <from_pos> <to_pos>' -> 'createItemListFromArea',
        'item_list clone <item_list> <name>' -> 'cloneItemList',
        'item_list delete <item_list>' -> ['deleteItemList', false],
        'item_list delete <item_list> confirm' -> ['deleteItemList', true],
        'item_list edit <item_list>' -> 'editItemList',
        'item_list edit <item_list> add items <items>' -> 'addEntriesToItemListFromItems',
        'item_list edit <item_list> add item_list <other_item_list>' -> 'addEntriesToItemListFromItemList',
        'item_list edit <item_list> remove items <entries>' -> 'removeEntriesFromItemListFromItems',
        'item_list edit <item_list> remove item_list <other_item_list>' -> 'removeEntriesFromItemListFromItemList',
        'item_list edit <item_list> remove index <index>' -> 'removeEntriesFromItemListWithIndex',
        'item_list list' -> 'listItemLists',
        'item_list info <item_list>' -> 'infoItemList',
        ...if(_checkVersion('1.4.57'), {'item_list view <item_list>' -> 'viewItemList'}, {}),

        'settings' -> 'settings',
        'settings shulker_box_color' -> 'printShulkerBoxColorSetting',
        'settings shulker_box_color <shulker_box_color>' -> 'setShulkerBoxColorSetting',
        'settings item_frame_type' -> 'printItemFrameTypeSetting',
        'settings item_frame_type <item_frame_type>' -> 'setItemFrameTypeSetting',
        'settings filter_item_amount' -> 'printFilterItemAmountSetting',
        'settings filter_item_amount <item_amount>' -> 'setFilterItemAmountSetting',
        'settings double_chest_direction' -> 'printDoubleChestDirectionSetting',
        'settings double_chest_direction <direction>' -> 'setDoubleChestDirectionSetting',
        'settings extra_containers_mode' -> 'printExtraContainersModeSetting',
        'settings extra_containers_mode <extra_containers_mode>' -> 'setExtraContainersModeSetting',
        'settings invalid_items_mode' -> 'printInvalidItemsModeSetting',
        'settings invalid_items_mode <invalid_items_mode>' -> 'setInvalidItemsModeSetting',
        'settings air_mode' -> 'printAirModeSetting',
        'settings air_mode <air_mode>' -> 'setAirModeSetting',
        'settings placing_offset' -> 'printPlacingOffsetSetting',
        'settings placing_offset <offset>' -> 'setPlacingOffsetSetting',
        'settings skip_empty_spots' -> 'printSkipEmptySpotsSetting',
        'settings skip_empty_spots <bool>' -> 'setSkipEmptySpotsSetting',
        'settings replace_blocks' -> 'printReplaceBlocksSetting',
        'settings replace_blocks <bool>' -> 'setReplaceBlocksSetting',
        'settings minimal_filling' -> 'printMinimalFillingSetting',
        'settings minimal_filling <bool>' -> 'setMinimalFillingSetting',

        'dummy_item stackable' -> 'printStackableDummyItem',
        'dummy_item stackable get' -> 'printStackableDummyItem',
        'dummy_item stackable give' -> 'giveStackableDummyItem',
        'dummy_item stackable set item <item>' -> 'setStackableDummyItem',
        'dummy_item stackable set hand' -> 'setStackableDummyItemFromHand',
        'dummy_item stackable reset' -> ['setStackableDummyItem', null],
        'dummy_item unstackable' -> 'printUnstackableDummyItem',
        'dummy_item unstackable get' -> 'printUnstackableDummyItem',
        'dummy_item unstackable give' -> 'giveUnstackableDummyItem',
        'dummy_item unstackable set item <item>' -> 'setUnstackableDummyItem',
        'dummy_item unstackable set hand' -> 'setUnstackableDummyItemFromHand',
        'dummy_item unstackable reset' -> ['setUnstackableDummyItem', null],

        ...if(_checkVersion('1.4.57'), {
            'item_filter <item_filter> view' -> ['viewItemFilter', null],
            'item_filter <item_filter> view <item>' -> 'viewItemFilter'
        }, {}),
        'item_filter <item_filter> fill <from_pos> <to_pos> items <stackable_items>' -> 'fillItemFiltersFromItems',
        'item_filter <item_filter> fill <from_pos> <to_pos> item_list <item_list>' -> 'fillItemFiltersFromItemList',
        'item_filter <item_filter> fill <from_pos> <to_pos> item_layout <item_layout>' -> 'fillItemFiltersFromItemLayout',
        'item_filter <item_filter> give items <stackable_items>' -> 'giveItemFilterFromItems',
        'item_filter <item_filter> give item_list <item_list>' -> 'giveItemFilterFromItemList',
        'item_filter <item_filter> give item_layout <item_layout>' -> 'giveItemFilterFromItemLayout',

        'container empty_boxes fill <from_pos> <to_pos>' -> ['fillContainersEmptyBoxes', 1],
        'container empty_boxes fill <from_pos>' -> ['fillContainersEmptyBoxes', null, 1],
        'container empty_boxes fill' -> ['fillContainersEmptyBoxes', null, null, 1],
        'container empty_boxes give' -> ['giveEmptyBoxesContainer', 1],
        'container empty_boxes_stacked fill <from_pos> <to_pos>' -> ['fillContainersEmptyBoxes', 64],
        'container empty_boxes_stacked fill <from_pos>' -> ['fillContainersEmptyBoxes', null, 64],
        'container empty_boxes_stacked fill' -> ['fillContainersEmptyBoxes', null, null, 64],
        'container empty_boxes_stacked give' -> ['giveEmptyBoxesContainer', 64],

        'container full fill <from_pos> <to_pos> items <items>' -> ['fillContainersFromItems', 'full'],
        'container full fill <from_pos> <to_pos> item_list <item_list>' -> ['fillContainersFromItemList', 'full'],
        'container full fill <from_pos> <to_pos> item_layout <item_layout>' -> ['fillContainersFromItemLayout', 'full'],
        'container full give <container> items <items>' -> ['giveContainerFromItems', 'full'],
        'container full give <container> item_list <item_list>' -> ['giveContainerFromItemList', 'full'],
        'container full give <container> item_layout <item_layout>' -> ['giveContainerFromItemLayout', 'full'],

        'container full_boxes fill <from_pos> <to_pos> items <items>' -> ['fillContainersFromItems', 'full_boxes'],
        'container full_boxes fill <from_pos> <to_pos> item_list <item_list>' -> ['fillContainersFromItemList', 'full_boxes'],
        'container full_boxes fill <from_pos> <to_pos> item_layout <item_layout>' -> ['fillContainersFromItemLayout', 'full_boxes'],
        'container full_boxes give <container> items <items>' -> ['giveContainerFromItems', 'full_boxes'],
        'container full_boxes give <container> item_list <item_list>' -> ['giveContainerFromItemList', 'full_boxes'],
        'container full_boxes give <container> item_layout <item_layout>' -> ['giveContainerFromItemLayout', 'full_boxes'],

        'container prefill fill <from_pos> <to_pos> items <items>' -> ['fillContainersFromItems', 'prefill'],
        'container prefill fill <from_pos> <to_pos> item_list <item_list>' -> ['fillContainersFromItemList', 'prefill'],
        'container prefill fill <from_pos> <to_pos> item_layout <item_layout>' -> ['fillContainersFromItemLayout', 'prefill'],
        'container prefill give <container> items <items>' -> ['giveContainerFromItems', 'prefill'],
        'container prefill give <container> item_list <item_list>' -> ['giveContainerFromItemList', 'prefill'],
        'container prefill give <container> item_layout <item_layout>' -> ['giveContainerFromItemLayout', 'prefill'],

        'container prefill_unstackables fill <from_pos> <to_pos> items <items>' -> ['fillContainersFromItems', 'prefill_unstackables'],
        'container prefill_unstackables fill <from_pos> <to_pos> item_list <item_list>' -> ['fillContainersFromItemList', 'prefill_unstackables'],
        'container prefill_unstackables fill <from_pos> <to_pos> item_layout <item_layout>' -> ['fillContainersFromItemLayout', 'prefill_unstackables'],
        'container prefill_unstackables give <container> items <items>' -> ['giveContainerFromItems', 'prefill_unstackables'],
        'container prefill_unstackables give <container> item_list <item_list>' -> ['giveContainerFromItemList', 'prefill_unstackables'],
        'container prefill_unstackables give <container> item_layout <item_layout>' -> ['giveContainerFromItemLayout', 'prefill_unstackables'],

        'container single_item fill <from_pos> <to_pos> items <items>' -> ['fillContainersFromItems', 'single_item'],
        'container single_item fill <from_pos> <to_pos> item_list <item_list>' -> ['fillContainersFromItemList', 'single_item'],
        'container single_item fill <from_pos> <to_pos> item_layout <item_layout>' -> ['fillContainersFromItemLayout', 'single_item'],
        'container single_item give <container> items <items>' -> ['giveContainerFromItems', 'single_item'],
        'container single_item give <container> item_list <item_list>' -> ['giveContainerFromItemList', 'single_item'],
        'container single_item give <container> item_layout <item_layout>' -> ['giveContainerFromItemLayout', 'single_item'],

        'container single_stack fill <from_pos> <to_pos> items <items>' -> ['fillContainersFromItems', 'single_stack'],
        'container single_stack fill <from_pos> <to_pos> item_list <item_list>' -> ['fillContainersFromItemList', 'single_stack'],
        'container single_stack fill <from_pos> <to_pos> item_layout <item_layout>' -> ['fillContainersFromItemLayout', 'single_stack'],
        'container single_stack give <container> items <items>' -> ['giveContainerFromItems', 'single_stack'],
        'container single_stack give <container> item_list <item_list>' -> ['giveContainerFromItemList', 'single_stack'],
        'container single_stack give <container> item_layout <item_layout>' -> ['giveContainerFromItemLayout', 'single_stack'],

        'empty' -> ['fillContainersEmpty', null, null],
        'empty <from_pos>' -> ['fillContainersEmpty', null],
        'empty <from_pos> <to_pos>' -> 'fillContainersEmpty',

        'mis_chest fill <from_pos> <to_pos> <item_lists>' -> 'fillMISChests',
        'mis_chest give <item_lists>' -> 'giveMISChest',

        'encoder_chest <signal_strength> fill <from_pos> <to_pos> <item_lists>' -> 'fillEncoderChests',
        'encoder_chest <signal_strength> give <item_lists>' -> 'giveEncoderChest',

        'item_frame place <from_pos> <to_pos> <facing> items <items>' -> 'placeItemFramesFromItems',
        'item_frame place <from_pos> <to_pos> <facing> item_list <item_list>' -> 'placeItemFramesFromItemList',
        'item_frame place <from_pos> <to_pos> <facing> item_layout <item_layout>' -> 'placeItemFramesFromItemLayout',
        'item_frame give items <items>' -> 'giveItemFramesFromItems',
        'item_frame give item_list <item_list>' -> 'giveItemFramesFromItemList',
        'item_frame give item_layout <item_layout>' -> 'giveItemFramesFromItemLayout',

        'chests full_boxes items <items>' -> ['giveChestsFromItems', 'full_boxes'],
        'chests full_boxes item_list <item_list>' -> ['giveChestsFromItemList', 'full_boxes'],
        'chests full_boxes item_layout <item_layout>' -> ['giveChestsFromItemLayout', 'full_boxes'],
        'chests stacks items <items>' -> ['giveChestsFromItems', 'stacks'],
        'chests stacks item_list <item_list>' -> ['giveChestsFromItemList', 'stacks'],
        'chests stacks item_layout <item_layout>' -> ['giveChestsFromItemLayout', 'stacks'],
        'chests single_items items <items>' -> ['giveChestsFromItems', 'single_items'],
        'chests single_items item_list <item_list>' -> ['giveChestsFromItemList', 'single_items'],
        'chests single_items item_layout <item_layout>' -> ['giveChestsFromItemLayout', 'single_items'],

        'full_box <item>' -> ['getFullBox', null],
        'full_box <item> <shulker_box_color>' -> 'getFullBox',

        'bundle items <items>' -> 'getBundleFromItems',
        'bundle item_list <item_list>' -> 'getBundleFromItemList',
        'bundle item_layout <item_layout>' -> 'getBundleFromItemLayout'
    },
    'arguments' -> {
        'name' -> {
            'type' -> 'term',
            'suggest' -> [],
            'case_sensitive' -> false
        },
        'item_list' -> {
            'type' -> 'term',
            'suggester' -> _(args) -> map(list_files('item_lists', 'shared_text'), slice(_, length('item_lists') + 1)),
            'case_sensitive' -> false
        },
        'other_item_list' -> {
            'type' -> 'term',
            'suggester' -> _(args) -> map(list_files('item_lists', 'shared_text'), slice(_, length('item_lists') + 1)),
            'case_sensitive' -> false
        },
        'index' -> {
            'type' -> 'int',
            'min' -> 0,
            'suggester' -> _(args) -> (
                item_list = args:'item_list';
                items = _readItemList(item_list);
                if(items, return([range(length(items))]));
            )
        },
        'entries' -> {
            'type' -> 'text',
            'suggester' -> _(args) -> (
                item_list = args:'item_list';
                input = args:'entries';
                entries = split('\\s+', input);
                items = filter(map(_readItemList(item_list), _:0), entries~_ == null);
                if(entries && slice(input, -1) != ' ', delete(entries, -1));
                return(if(entries, map(items, str('%s %s', join(' ', entries), _)), items));
            ),
            'case_sensitive' -> false
        },
        'items' -> {
            'type' -> 'text',
            'suggester' -> _(args) -> (
                input = args:'items';
                items = split('\\s+', input);
                if(items && slice(input, -1) != ' ', delete(items, -1));
                return(if(items, map(item_list(), str('%s %s', join(' ', items), _)), item_list()));
            ),
            'case_sensitive' -> false
        },
        'stackable_items' -> {
            'type' -> 'text',
            'suggester' -> _(args) -> (
                input = args:'stackable_items';
                items = split('\\s+', input);
                stackable_items = filter(item_list(), stack_limit(_) != 1);
                if(items && slice(input, -1) != ' ', delete(items, -1));
                return(if(items, map(stackable_items, str('%s %s', join(' ', items), _)), stackable_items));
            ),
            'case_sensitive' -> false
        },
        'item_lists' -> {
            'type' -> 'text',
            'suggester' -> _(args) -> (
                input = args:'item_lists';
                entries = split('\\s+', input);
                item_lists = map(list_files('item_lists', 'shared_text'), slice(_, length('item_lists') + 1));
                if(entries && slice(input, -1) != ' ', delete(entries, -1));
                return(if(entries, map(item_lists, str('%s %s', join(' ', entries), _)), item_lists));
            ),
            'case_sensitive' -> false
        },
        'item_filter' -> {
            'type' -> 'term',
            'options' -> keys(global_item_filters),
            'case_sensitive' -> false
        },
        'item_layout' -> {
            'type' -> 'term',
            'suggester' -> _(args) -> map(list_files('item_layouts', 'shared_text'), slice(_, length('item_layouts') + 1)),
            'case_sensitive' -> false
        },
        'module' -> {
            'type' -> 'term',
            'options' -> map(global_modules, _:'name'),
            'suggest' -> []
        },
        'container' -> {
            'type' -> 'term',
            'options' -> global_containers,
            'case_sensitive' -> false
        },
        'from_pos' -> {
            'type' -> 'pos',
            'loaded' -> true
        },
        'to_pos' -> {
            'type' -> 'pos',
            'loaded' -> true
        },
        'direction' -> {
            'type' -> 'term',
            'options' -> keys(global_double_chest_direction_options),
            'case_sensitive' -> false
        },
        'mask' -> {
            'type' -> 'blockpredicate'
        },
        'signal_strength' -> {
            'type' -> 'int',
            'min' -> 1,
            'suggest' -> [1, 2]
        },
        'item_amount' -> {
            'type' -> 'int',
            'min' -> 1,
            'max' -> 63,
            'suggest' -> []
        },
        'shulker_box_color' -> {
            'type' -> 'term',
            'options' -> keys(global_shulker_box_colors),
            'case_sensitive' -> false
        },
        'item_frame_type' -> {
            'type' -> 'term',
            'options' -> keys(global_item_frame_types),
            'case_sensitive' -> false
        },
        'offset' -> {
            'type' -> 'int',
            'min' -> 1,
            'suggest' -> []
        },
        'extra_containers_mode' -> {
            'type' -> 'term',
            'options' -> keys(global_extra_container_mode_options),
            'case_sensitive' -> false
        },
        'invalid_items_mode' -> {
            'type' -> 'term',
            'options' -> keys(global_invalid_items_mode_options),
            'case_sensitive' -> false
        },
        'air_mode' -> {
            'type' -> 'term',
            'options' -> keys(global_air_mode_options),
            'case_sensitive' -> false
        },
        'facing' -> {
            'type' -> 'term',
            'options' -> global_directions,
            'case_sensitive' -> false
        }
    },
    'resources' -> [
        {
        'source' -> 'https://raw.githubusercontent.com/CommandLeo/scarpet/main/resources/stx/stx_modules.json',
        'target' -> 'stx_modules.json'
        }
    ],
    'requires' -> {
        'carpet' -> '>=1.4.44'
    },
    'scope' -> 'global'
};

// HELPER FUNCTIONS

_error(error) -> (
    print(format(str('r %s', error)));
    run(str('playsound block.note_block.didgeridoo master %s', player()~'command_name'));
    exit();
);

_removeDuplicates(list) -> filter(list, list~_ == _i);

// UTILITY FUNCTIONS

_readItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    regex = '(\\w+)(\\{.+\\})?';
    entries = map(filter(read_file(item_list_path, 'shared_text'), _), results = _~regex; [lower(results:0), results:1 || null]);
    return(entries);    
);

_itemToString(item_tuple) -> (
    [item, nbt] = item_tuple;
    return(item + (nbt || ''));
);

_scanStrip(from_pos, to_pos) -> (
    [x1, y1, z1] = from_pos;
    [x2, y2, z2] = to_pos;
    [dx, dy, dz] = map(to_pos - from_pos, if(_ < 0, -1, 1));

    if(
        x1 != x2,
            return(map(range(x1, x2 + dx, dx), block([_, y1, z1]))),
        y1 != y2,
            return(map(range(y1, y2 + dy, dy), block([x1, _, z1]))),
        z1 != z2,
            return(map(range(z1, z2 + dz, dz), block([x1, y1, _])))
    );
    return([block(from_pos)]);
);

_scanStripes(from_pos, to_pos) -> (
   [x1, y1, z1] = from_pos;
   [x2, y2, z2] = to_pos;
   [dx, dy, dz] = map(to_pos - from_pos, abs(_));
   [sx, sy, sz] = map(to_pos - from_pos, if(_ < 0, -1, 1));

   return(if(
    dx >= dy && dx >= dz,
        map(range(x1, x2 + sx, sx), l = []; volume(_, y1, z1, _, y2, z2, l += _); l),
    dz >= dx && dz >= dy,
        map(range(z1, z2 + sz, sz), l = []; volume(x1, y1, _, x2, y2, _, l += _); l),
    dy >= dx && dy >= dz,
        map(range(y1, y2 + sy, sy), l = []; volume(x1, _, z1, x2, _, z2, l += _); l)
   ));
);

_isInvalidItem(item) -> (
    return(item_list()~item == null || (item == 'air' && global_air_mode == 'invalid'));
);

_handleInvalidItems(item_tuples) -> (
    invalid_items = filter(map(item_tuples, _:0), _isInvalidItem(_));
    if(invalid_items,
        if(
            global_invalid_items_mode == 'error', _error(str('Invalid items: %s', join(', ', sort(_removeDuplicates(invalid_items))))),
            global_invalid_items_mode == 'skip', print(format('f » ', str('g WARNING! Skipped the following invalid items: %s', sort(_removeDuplicates(invalid_items))))),
            print(format('f » ', str('g Ignored the following invalid items: %s', sort(_removeDuplicates(invalid_items)))))
        );
    );
);

_handleUnstackableItems(item_tuples) -> (
    unstackable_items = filter(map(item_tuples, _:0), !_isInvalidItem(_) && stack_limit(_) == 1);
    if(unstackable_items,
        if(
            global_invalid_items_mode == 'error', _error(str('You can\'t use unstackables: %s', join(', ', sort(_removeDuplicates(unstackable_items))))),
            global_invalid_items_mode == 'skip', print(format('f » ', str('g WARNING! Skipped the following unstackable items: %s', sort(_removeDuplicates(unstackable_items))))),
            print(format('f » ', str('g Ignored the following unstackable items: %s', sort(_removeDuplicates(unstackable_items)))))
        );
    );
);

_getReadingComparators(pos) -> (
    comparators = [];
    map(['north', 'east', 'south', 'west'],
        block1 = block(pos_offset(pos, _, -1));
        if(block1 == 'comparator' && block_state(block1, 'facing') == _, continue(comparators += block1));
        if(solid(block1) && inventory_has_items(block1) == null,
            block2 = block(pos_offset(block1, _, -1));
            if(block2 == 'comparator' && block_state(block2, 'facing') == _, comparators += block2);
        );
    );
    return(comparators);
);

_updateComparators(block) -> (
    for(_getReadingComparators(block), update(_));

    return(block);
);

_getFacing(player) -> (
    facing = query(player, 'facing');
    return(if(facing == 'down' || facing == 'up', query(player, 'facing', 1), facing));
);

_getItemFromBlock(block) -> (
    if(!block, return());
    if(item_list()~block != null, return(str(block)));

    [x, y, z] = pos(block);
    dummy_y = -1e5;
    run(str('loot spawn %d %d %d mine %d %d %d shears{Enchantments:[{id:silk_touch,lvl:1}]}', x, dummy_y, z, x, y, z));
    items = entity_area('item', [x, dummy_y, z], [0.5, 0.5, 0.5]);
    item = items:(-1);
    for(items, modify(_, 'remove'));
    return(if(item, item~'item':0));
);

_generateFullBoxNbt(item, nbt) -> (
    return(
        {
            'BlockEntityTag' -> {
                'Items' -> map(range(27), {'id' -> item, 'Count' -> stack_limit(item), 'Slot' -> _, ...if(nbt, {'tag' -> nbt}, {})})
            }
        }
    );
);

// MAIN

menu() -> (
    texts = [
       'fs ' + ' ' * 80, ' \n',
        '#01A3A4b StorageTechX', if(_checkVersion('1.4.57'), '@https://github.com/CommandLeo/scarpet/wiki/StorageTechX'), 'g  by ', str('%sb CommandLeo', global_color), '^g https://github.com/CommandLeo', if(_checkVersion('1.4.57'), '@https://github.com/CommandLeo'),' \n\n',
        'g A suite of tools for Storage Tech.', '  \n',
        'g Run ', str('%s /%s help', global_color, system_info('app_name')), str('!/%s help', system_info('app_name')), '^g Click to run the command', 'g  to see a list of all the commands.', '  \n',
        'fs ' + ' ' * 80
    ];
    print(format(texts));
);

help() -> (
    print('s∞n™, in the meanwhile use /help stx');
);

// MODULES

listModules() -> (
    app_list = system_info('app_list');
    texts = [
        'fs ' + ' ' * 80, ' \n',
        str('%sb StorageTechX Modules', global_color), ' \n\n',
        ...reduce(global_modules, module = _:'name'; [..._a, 'f 【', ...if(app_list~module != null, ['#2ECC71 ✔', '^#2ECC71 Installed'], ['g ↓', '^g Click to install', str('?/%s modules install %s', system_info('app_name'), module)]), 'f 】 ', str('%s %s', _:'color', _:'display_name'), str('^%s %s.sc', _:'color', module), if(_checkVersion('1.4.57'), str('@%s', _:'wiki')), ' \n'], []),
        'fs ' + ' ' * 80
    ];
    print(format(texts));
);

installModule(module) -> (
    module_data = first(global_modules, _:'name' == module);
    if(!module_data, _error('There is no module with that name'));

    print(format('f » ', 'g Installing the module...'));

    current_appstore = system_info('world_carpet_rules'):'scriptsAppStore';
    new_appstore = 'CommandLeo/scarpet/contents/programs';
    run(str('carpet scriptsAppStore %s', new_appstore));
    command_output = run(str('script download %s.sc', module));
    run(str('carpet scriptsAppStore %s', current_appstore));

    if(command_output:2, exit(print(format('r There was an error while trying to download the module', str('^r %s', command_output:2)))));
    print(format('f » ', 'g Successfully installed the ', str('%s %s', module_data:'color', module_data:'display_name'), 'g  module'));
);

// ITEM LISTS

createItemListFromItems(name, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    createItemList(name, items);
);

createItemListFromItemLayout(name, item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str('The item layout %s doesn\'t exist', item_layout)));

    items = map(read_file(item_layout_path, 'shared_text'), [lower(_), null]);
    if(!items, _error(str('The %s item layout is empty', item_layout)));

    createItemList(name, items);
);

createItemListFromContainer(name, position) -> (
    target = if(position, block(position), player()~'trace');
    if(!target || inventory_has_items(target) == null || target~'type' == 'player', _error('Invalid container'));
    if(!inventory_has_items(target), _error('The container is empty'));

    items = [];
    loop(inventory_size(target), item_tuple = inventory_get(target, _); if(item_tuple, [item, count, nbt] = item_tuple; items += [item, nbt]));
    createItemList(name, _removeDuplicates(items));
);

createItemListFromArea(name, from_pos, to_pos) -> (
    items = {};
    ignored_blocks = {};
    volume(from_pos, to_pos, item = _getItemFromBlock(_); if(item, items += [item, null], ignored_blocks += str(_)));

    if(ignored_blocks, print(format('f » ', str('g The following blocks were ignored: %s', join(', ', keys(ignored_blocks))))));
    if(!items, _error('The selected area is empty'));

    createItemList(name, keys(items));
);

createItemList(name, items) -> (
    item_list_path = str('item_lists/%s', name);
    if(list_files('item_lists', 'shared_text')~item_list_path != null, _error('An item list with that name already exists'));
    if(!items, _error('No items were provided'));
    
    invalid_items = filter(map(items, _:0), item_list()~_ == null);
    if(invalid_items, _error(str('Invalid items: %s', join(', ', sort(_removeDuplicates(invalid_items))))));

    success = write_file(item_list_path, 'shared_text', map(items, _itemToString(_)));

    if(!success, _error('There was an error while writing the file'));
    print(format('f » ', 'g Successfully created the ', str('%s %s', global_color, name), 'g  item list'));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

cloneItemList(item_list, name) -> (
    item_list_path = str('item_lists/%s', item_list);
    new_item_list_path = str('item_lists/%s', name);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));
    if(list_files('item_lists', 'shared_text')~new_item_list_path != null, _error('An item list with that name already exists'));

    success = write_file(new_item_list_path, 'shared_text', read_file(item_list_path, 'shared_text'));
    if(!success, _error('There was an error while writing the file'));
    print(format('f » ', 'g Successfully cloned the ', str('%s %s', global_color, item_list), 'g  item list into the ', str('%s %s', global_color, name), 'g  item list'));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

deleteItemList(item_list, confirmation) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    if(!confirmation, exit(print(format('f » ', 'g Click ', str('%sbu here', global_color), str('^g /%s item_list delete %s confirm', system_info('app_name'), item_list), str('!/%s item_list delete %s confirm', system_info('app_name'), item_list),'g  to confirm the deletion of the ', str('%s %s', global_color, item_list), 'g  item list'))));
    success = delete_file(item_list_path, 'shared_text');

    if(!success, _error('There was an error while deleting the file'));
    print(format('f » ', 'g Successfully deleted the ', str('%s %s', global_color, item_list), 'g  item list'));
    run(str('playsound item.shield.break master %s', player()~'command_name'));
);

editItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));
    items = _readItemList(item_list);
    if(!items, _error(str('The %s item list is empty', item_list)));

    print(format(reduce(items, [item, nbt] = _; [..._a, ' \n  ', '#EB4D4Bb ❌', '^r Remove the item', str('?/%s item_list edit %s remove index %d', system_info('app_name'), item_list, _i), '  ', str('g%s %s%s', if(item_list()~item == null, 's', ''), item, if(nbt, '*', '')), str('!/give @s %s', _itemToString(_)), if(item_list()~item == null, '^g Invalid item', nbt, str('^g %s', nbt), ''), str('!/give @s %s', _itemToString(_))], ['f » ', 'g Edit the ', str('%s %s', global_color, item_list), 'g  item list: ', '#26DE81b (+)', '^#26DE81 Add more items', str('?/%s item_list edit %s add', system_info('app_name'), item_list)])));
);

addEntriesToItemListFromItems(item_list, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    addEntriesToItemList(item_list, items);
);

addEntriesToItemListFromItemList(item_list, other_item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    other_item_list_path = str('item_lists/%s', other_item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));
    if(list_files('item_lists', 'shared_text')~other_item_list_path == null, _error(str('The item list %s doesn\'t exist', other_item_list)));

    addEntriesToItemList(item_list, _readItemList(other_item_list));
);

addEntriesToItemList(item_list, entries) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    items = _readItemList(item_list);
    if(!items, _error('No items to add to the item list'));

    invalid_items = filter(map(items, _:0), item_list()~_ == 'null');
    if(invalid_items, _error(str('Invalid items: %s', join(', ', sort(_removeDuplicates(invalid_items))))));

    success = write_file(item_list_path, 'shared_text', map(entries, _itemToString(_)));

    if(!success, _error('There was an error while writing the file'));
    print(format('f » ', 'g Successfully added the items to the ', str('%s %s', global_color, item_list), 'g  item list'));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

removeEntriesFromItemListFromItems(item_list, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    removeEntriesFromItemList(item_list, items);
);

removeEntriesFromItemListFromItemList(item_list, other_item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    other_item_list_path = str('item_lists/%s', other_item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));
    if(list_files('item_lists', 'shared_text')~other_item_list_path == null, _error(str('The item list %s doesn\'t exist', other_item_list)));

    removeEntriesFromItemList(item_list, _readItemList(other_item_list));
);

removeEntriesFromItemList(item_list, entries) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    items = _readItemList(item_list);

    l1 = length(items);
    items = filter(items, [item, nbt] = _; entries~[item, nbt] == null && entries~[item, null] == null);
    l2 = length(items);
    if(l2 == l1, _error('No entries to remove from the item list'));

    delete_file(item_list_path, 'shared_text');
    success = write_file(item_list_path, 'shared_text', map(items, _itemToString(_)));

    if(!success, _error('There was an error while writing the file'));
    print(format('f » ', str('g Successfully removed %d items from the ', l1 - l2), str('%s %s', global_color, item_list), 'g  item list'));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

removeEntriesFromItemListWithIndex(item_list, index) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    items = _readItemList(item_list);
    if(index < 0 || index >= length(items), _error('Invalid index'));

    delete(items:index);

    delete_file(item_list_path, 'shared_text');
    success = write_file(item_list_path, 'shared_text', map(items, _itemToString(_)));

    if(!success, _error('There was an error while writing the file'));
    print(format('f » ', 'g Successfully removed the item from the ', str('%s %s', global_color, item_list), 'g  item list'));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

listItemLists() -> (
    files = list_files('item_lists', 'shared_text');
    if(!files, _error('There are no item lists saved'));

    item_lists = map(files, slice(_, length('item_lists') + 1));

    texts = reduce(item_lists, [..._a, if(_i == 0, '', 'g , '), str('%s %s', global_color, _), str('^g /%s item_list info %s', system_info('app_name'), _), str('?/%s item_list info %s', system_info('app_name'), _)], ['f » ', 'g Available item lists: ']);
    print(format(texts));
);

infoItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str('The %s item list is empty', item_list)));

    texts = reduce(items, [item, nbt] = _; [..._a, if(length(items) < 12, ' \n', if(_i == 0, '', 'g , ')), str('g %s', if(length(items) < 12, '   ', '')), str('g%s %s%s', if(item_list()~item == null, 's', ''), item, if(nbt, '*', '')), str('!/give @s %s', _itemToString(_)), if(item_list()~item == null, '^g Invalid item', nbt, str('^g %s', nbt), '')], ['f » ', 'g The ', str('%s %s', global_color, item_list), 'g  item list contains the following items: ']);
    print(format(texts));
);

viewItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    entries = _readItemList(item_list);
    items = filter(entries, [item, nbt] = _; item != 'air' && item_list()~item != null);
    if(!items, _error(str('The %s item list is empty', item_list)));

    pages = map(range(length(items) / 45), slice(items, _i * 45, min(length(items), (_i + 1) * 45)));

    _setMenuInfo(screen, page_count, pages_length, items_length) -> (
        inventory_set(screen, 49, 1, 'paper', str('{pageNumber:%s,display:{Name:\'{"text":"Page %d/%d","color":"%s","italic":false}\',Lore:[\'{"text":"%s entries","color":"gray","italic":false}\']}}', page_count, page_count % pages_length + 1, pages_length, global_color, items_length));
    );

    _setMenuItems(screen, page) -> (
        loop(45, inventory_set(screen, _, if(_ < length(page), 1, 0), ...page:_));
    );

    screen = create_screen(player(), 'generic_9x6', item_list, _(screen, player, action, data, outer(pages), outer(items)) -> (
        if(length(pages) > 1 && action == 'pickup' && (data:'slot' == 48 || data:'slot' == 50),
            page_number = inventory_get(screen, 49):2:'pageNumber';
            page = if(data:'slot' == 48, pages:(page_number += -1), data:'slot' == 50, pages:(page_number += 1));
            _setMenuInfo(screen, page_number, length(pages), length(items));
            _setMenuItems(screen, page);
        );
        if(action == 'pickup_all' || action == 'quick_move' || (action != 'clone' && data:'slot' != null && 0 <= data:'slot' <= 44) || (45 <= data:'slot' <= 53), return('cancel'));
    ));

    _setMenuItems(screen, pages:global_page);

    for(range(45, 54), inventory_set(screen, _, 1, 'gray_stained_glass_pane', '{display:{Name:\'""\'}}'));
    _setMenuInfo(screen, 0, length(pages), length(items));
    if(length(pages) > 1,
        inventory_set(screen, 48, 1, 'spectral_arrow', str('{display:{Name:\'{"text":"Previous page","color":"%s","italic":false}\'}}', global_color));
        inventory_set(screen, 50, 1, 'spectral_arrow', str('{display:{Name:\'{"text":"Next page","color":"%s","italic":false}\'}}', global_color));
    );
);

// SETTINGS

settings() -> (
    texts = [
        'fs ' + ' ' * 80, ' \n',
        str('%sb StorageTechX General Settings', global_color), ' \n\n',
        'g Shulker Box Color', '^g The color of shulker boxes', str('?/%s settings shulker_box_color', system_info('app_name')), 'f  - ', str('%sb %s', global_shulker_box_colors:global_shulker_box_color, upper(global_shulker_box_color)), ' \n',
        'g Item Frame Type', '^g The type of item frame to use (normal or glowing)', str('?/%s settings item_frame_type', system_info('app_name')), 'f  - ', str('db %s', upper(global_item_frame_types:global_item_frame_type)), ' \n',
        'g Filter Item Amount', '^g The amount of filter items in the first slot of low threshold item filters', str('?/%s settings filter_item_amount', system_info('app_name')), 'f  - ', str('%sb %s', global_color, global_filter_item_amount), ' \n',
        'g Double Chest Direction', '^g The direction where to place the second half of double chests', str('?/%s settings double_chest_direction', system_info('app_name')), 'f  - ', str('wb %s', upper(global_double_chest_direction)), ' \n',
        'g Extra Containers Mode', '^g How extra containers should be filled', str('?/%s settings extra_containers_mode', system_info('app_name')), 'f  - ', str('%sb %s', global_extra_container_mode_options:global_extra_containers_mode, upper(global_extra_containers_mode)), ' \n',
        'g Invalid Items Mode', '^g How invalid items should be handled', str('?/%s settings invalid_items_mode', system_info('app_name')), 'f  - ', str('%sb %s', global_invalid_items_mode_options:global_invalid_items_mode, upper(global_invalid_items_mode)), ' \n',
        'g Air Mode', '^g How air items should be handled', str('?/%s settings air_mode', system_info('app_name')), 'f  - ', str('%sb %s', global_air_mode_options:global_air_mode, upper(global_air_mode)), ' \n',
        'g Placing Offset', '^g The offset between blocks being placed', str('?/%s settings placing_offset', system_info('app_name')), 'f  - ', str('%sb %s', global_color, global_placing_offset), ' \n',
        'g Skip Empty Spots', '^g Whether to skip empty spots', str('?/%s settings skip_empty_spots', system_info('app_name')), 'f  - ', if(global_skip_empty_spots, 'lb TRUE', 'rb FALSE'), ' \n',
        'g Replace blocks', '^g Whether to replace non-air blocks when placing blocks', str('?/%s settings replace_blocks', system_info('app_name')), 'f  - ', if(global_replace_blocks, 'lb TRUE', 'rb FALSE'), ' \n',
        'g Minimal Filling', '^g Whether to fill the first slot of standard item filters with only a single filter item', str('?/%s settings minimal_filling', system_info('app_name')), 'f  - ', if(global_minimal_filling, 'lb TRUE', 'rb FALSE'), ' \n',
        'fs ' + ' ' * 80
    ];
    print(format(texts));
);

printShulkerBoxColorSetting() -> (
    print(format('f » ', 'g The shulker box color is ', str('%sb %s', global_shulker_box_colors:global_shulker_box_color, upper(global_shulker_box_color))));
);

setShulkerBoxColorSetting(shulker_box_color) -> (
    global_shulker_box_color = shulker_box_color;
    print(format('f » ', 'g The shulker box color has been set to ', str('%sb %s', global_shulker_box_colors:shulker_box_color, upper(shulker_box_color))));
);

printItemFrameTypeSetting() -> (
    print(format('f » ', 'g The item frame type is ', str('db %s', upper(global_item_frame_types:global_item_frame_type))));
);

setItemFrameTypeSetting(item_frame_type) -> (
    global_item_frame_type = item_frame_type;
    print(format('f » ', 'g The item frame type has been set to ', str('db %s', upper(global_item_frame_types:item_frame_type))));
);

printFilterItemAmountSetting() -> (
    print(format('f » ', 'g The amount of filter items for low threshold filters is ', str('%s %d', global_color, global_filter_item_amount)));
);

setFilterItemAmountSetting(amount) -> (
    global_filter_item_amount = amount;
    print(format('f » ', 'g The amount of filter items for low threshold filters has been set to ', str('%s %d', global_color, amount)));
);

printDoubleChestDirectionSetting() -> (
    print(format('f » ', 'g The direction where to place the second half of double chests is ', str('wb %s', upper(global_double_chest_direction))));
);

setDoubleChestDirectionSetting(direction) -> (
    global_double_chest_direction = direction;
    print(format('f » ', 'g The direction where to place the second half of double chests has been set to ', str('wb %s', upper(direction))));
);

printExtraContainersModeSetting() -> (
    print(format('f » ', 'g The mode to fill extra container is ', str('%sb %s', global_extra_container_mode_options:global_extra_containers_mode, upper(global_extra_containers_mode))));
);

setExtraContainersModeSetting(extra_containers_mode) -> (
    global_extra_containers_mode = extra_containers_mode;
    print(format('f » ', 'g The mode to fill extra container has been set to ', str('%sb %s', global_extra_container_mode_options:extra_containers_mode, upper(extra_containers_mode))));
);

printInvalidItemsModeSetting() -> (
    print(format('f » ', 'g The mode to handle invalid items is ', str('%sb %s', global_invalid_items_mode_options:global_invalid_items_mode, upper(global_invalid_items_mode))));
);

setInvalidItemsModeSetting(invalid_items_mode) -> (
    global_invalid_items_mode = invalid_items_mode;
    print(format('f » ', 'g The mode to handle invalid items has been set to ', str('%sb %s', global_invalid_items_mode_options:invalid_items_mode, upper(invalid_items_mode))));
);

printAirModeSetting() -> (
    print(format('f » ', 'g The mode to handle air items is ', str('%sb %s', global_air_mode_options:global_air_mode, upper(global_air_mode))));
);

setAirModeSetting(air_mode) -> (
    global_air_mode = air_mode;
    print(format('f » ', 'g The mode to handle air items has been set to ', str('%sb %s', global_air_mode_options:air_mode, upper(air_mode))));
);

printPlacingOffsetSetting() -> (
    print(format('f » ', 'g The placing offset is ', str('%s %d', global_color, global_placing_offset)));
);

setPlacingOffsetSetting(offset) -> (
    global_placing_offset = offset;
    print(format('f » ', 'g The placing offset has been set to ', str('%s %d', global_color, offset)));
);

printReplaceBlocksSetting() -> (
    print(format('f » ', 'g Replacing blocks is ', if(global_replace_blocks, 'l enabled', 'r disabled')));
); 

setReplaceBlocksSetting(bool) -> (
    global_replace_blocks = bool;
    print(format('f » ', 'g Replacing blocks has been ', if(bool, 'l enabled', 'r disabled')));
);

printSkipEmptySpotsSetting() -> (
    print(format('f » ', 'g Skipping empty spots is ', if(global_skip_empty_spots, 'l enabled', 'r disabled')));
);

setSkipEmptySpotsSetting(bool) -> (
    global_skip_empty_spots = bool;
    print(format('f » ', 'g Skipping empty spots has been ', if(bool, 'l enabled', 'r disabled')));
);

printMinimalFillingSetting() -> (
    print(format('f » ', 'g Minimal filling is ', if(global_minimal_filling, 'l enabled', 'r disabled')));
);

setMinimalFillingSetting(bool) -> (
    global_minimal_filling = bool;
    print(format('f » ', 'g Minimal filling has been ', if(bool, 'l enabled', 'r disabled')));
);

// DUMMY ITEM

printStackableDummyItem() -> (
    [item, nbt] = global_stackable_dummy_item;
    print(format('f » ', 'g The current stackable dummy item is ', if(nbt, ...[str('%s %s*', global_color, item), str('^g %s', nbt)], str('%s %s', global_color, item)))),
);

setStackableDummyItemFromHand() -> (
    holds = player()~'holds';
    if(!holds, _error('You are not holding any item'));

    setStackableDummyItem(holds);
);

setStackableDummyItem(item_tuple) -> (
    if(
        !item_tuple,
            global_stackable_dummy_item = global_default_stackable_dummy_item;
            print(format('f » ', 'g The stackable dummy item has been reset')),

            [item, count, nbt] = item_tuple;
            if(stack_limit(item) == 1, _error('You can use only stackable items'));
            global_stackable_dummy_item = [item, nbt];
            print(format('f » ', 'g The stackable dummy item has been set to ', str('%s %s', global_color, item), if(nbt, str('^g %s', nbt)))),
    );
);

giveStackableDummyItem() -> (
    [item, nbt] = global_stackable_dummy_item;
    run(str('give %s %s%s', player()~'command_name', item, nbt || ''));
);

printUnstackableDummyItem() -> (
    [item, nbt] = global_unstackable_dummy_item;
    print(format('f » ', 'g The current unstackable dummy item is ', if(nbt, ...[str('%s %s*', global_color, item), str('^g %s', nbt)], str('%s %s', global_color, item)))),
);

setUnstackableDummyItemFromHand() -> (
    holds = player()~'holds';
    if(!holds, _error('You are not holding any item'));

    setUnstackableDummyItem(holds);
);

setUnstackableDummyItem(item_tuple) -> (
    if(
        !item_tuple,
            global_unstackable_dummy_item = global_default_unstackable_dummy_item;
            print(format('f » ', 'g The unstackable dummy item has been reset')),

            [item, count, nbt] = item_tuple;
            if(stack_limit(item) != 1, _error('You can use only unstackable items'));
            global_unstackable_dummy_item = [item, nbt];
            print(format('f » ', 'g The unstackable dummy item has been set to ', str('%s %s', global_color, item), if(nbt, str('^g %s', nbt)))),
    );
);

giveUnstackableDummyItem() -> (
    [item, nbt] = global_unstackable_dummy_item;
    run(str('give %s %s%s', player()~'command_name', item, nbt || ''));
);

// ITEM FILTER

_itemFilter(hopper, item_filter, item_tuple) -> (
    if(
        item_filter == 'ssi_ss2', _ssiItemFilter(hopper, item_tuple, 2, global_filter_item_amount, global_minimal_filling),
        item_filter == 'ss3', _ssItemFilter(hopper, item_tuple, 3, global_minimal_filling),
        item_filter == 'overstacking', _overstackingItemFilter(hopper, item_tuple, global_filter_item_amount, global_minimal_filling),
        item_filter == 'box_sorter', _boxSorterFilter(hopper, item_tuple),
        item_filter == 'stack_separation', _stackSeparationFilter(hopper, item_tuple)
    );
);

_ssiItemFilter(hopper, item_tuple, signal_strength, amount, minimal) -> (
    [item, nbt] = item_tuple;
    total_dummy_amount = floor((ceil(5 * 64 / 14 * (signal_strength - 1)) - (amount + 1) * (64 / stack_limit(item))) / (64 / stack_limit(global_stackable_dummy_item:0)));

    inventory_set(hopper, 0, if(minimal, 1, amount), item, nbt);
    for(range(1, 5),
        amount = min(total_dummy_amount - 5 + _, 63) + 1;
        inventory_set(hopper, _, amount, ...global_stackable_dummy_item);
        total_dummy_amount += -amount;
    );

    return(hopper);
);

_ssItemFilter(hopper, item_tuple, signal_strength, minimal) -> (
    [item, nbt] = item_tuple;
    amount = floor((ceil(5 * 64 / 14 * (signal_strength - 1)) - 1 - 4 * (64 / stack_limit(global_stackable_dummy_item:0))) / (64 / stack_limit(item)));

    inventory_set(hopper, 0, if(minimal, 1, amount), item, nbt);
    for(range(1, 5), inventory_set(hopper, _, 1, ...global_stackable_dummy_item));

    return(hopper);
);

_overstackingItemFilter(hopper, item_tuple, amount, minimal) -> (
    [item, nbt] = item_tuple;
    dummy_amount = floor((64 - (amount + 1) * (64 / stack_limit(item))) / (64 / stack_limit(global_stackable_dummy_item:0)));

    inventory_set(hopper, 0, if(minimal, 1, amount), item, nbt);
    inventory_set(hopper, 1, dummy_amount, ...global_stackable_dummy_item);
    inventory_set(hopper, 2, 2, 'enchanted_book', {'StoredEnchantments' -> [{'id' -> 'vanishing_curse', 'lvl' -> 1}]});
    for(range(3, 5), inventory_set(hopper, _, 1, ...global_unstackable_dummy_item));

    return(hopper);
);

_boxSorterFilter(hopper, item_tuple) -> (
    [item, nbt] = item_tuple;

    inventory_set(hopper, 0, 1, item, nbt);
    for(range(1, 5), inventory_set(hopper, _, 1, 'shulker_box'));
    
    return(hopper);
);

_stackSeparationFilter(hopper, item_tuple) -> (
    [item, nbt] = item_tuple;

    inventory_set(hopper, 0, stack_limit(item) - 1, item, nbt);
    for(range(1, 5), inventory_set(hopper, _, 1, ...global_unstackable_dummy_item));
    
    return(hopper);
);

fillItemFiltersFromItems(item_filter, from_pos, to_pos, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    fillItemFilters(item_filter, items, from_pos, to_pos);
);

fillItemFiltersFromItemList(item_filter, from_pos, to_pos, item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str('The %s item list is empty', item_list)));

    fillItemFilters(item_filter, items, from_pos, to_pos);
);

fillItemFiltersFromItemLayout(item_filter, from_pos, to_pos, item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str('The item layout %s doesn\'t exist', item_layout)));

    items = map(read_file(item_layout_path, 'shared_text'), [lower(_), null]);
    if(!items, _error(str('The %s item layout is empty', item_layout)));

    fillItemFilters(item_filter, items, from_pos, to_pos);
);

fillItemFilters(item_filter, items, from_pos, to_pos) -> (
    if(global_item_filters~item_filter == null, _error(str('The %s item filter doesn\'t exist', item_filter)));
    if(!items, _error('No items were provided'));
    if(length(filter(to_pos - from_pos, _ == 0)) < 2, _error('The area must be a row of blocks'));

    _handleInvalidItems(items);
    _handleUnstackableItems(items);
    if(global_invalid_items_mode == 'skip', items = filter(items, !_isInvalidItem(_:0) && stack_limit(_:0) != 1));

    fallback_item = if(
        global_invalid_items_mode == 'dummy_item', global_stackable_dummy_item,
        global_invalid_items_mode == 'air', ['air', null]
    );

    i = 0;
    affected_blocks = for(_scanStrip(from_pos, to_pos),
        block = _;

        if(block != 'hopper',
            if(!global_skip_empty_spots, i += 1);
            continue();
        );

        [item, nbt] = if(
            global_extra_containers_mode == 'repeat' || length(items) == 1 || i < length(items), items:i,
            global_extra_containers_mode == 'dummy_item', global_stackable_dummy_item,
            global_extra_containers_mode == 'air', ['air', null],
            global_extra_containers_mode == 'no_fill', continue()
        );

        i += 1;

        if(item == 'air' && global_air_mode == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item) || stack_limit(item) == 1,
            if(!fallback_item, continue());
            [item, nbt] = fallback_item;
        );

        _itemFilter(block, item_filter, [item, nbt]);
        _updateComparators(block);
    );

    print(format('f » ', 'g Filled ', str('%s %s', global_color, affected_blocks), str('g  hopper%s with the ', if(affected_blocks == 1, '', 's')), str('%s %s', global_color, global_item_filters:item_filter), 'g  item filter'));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

giveItemFilterFromItems(item_filter, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    giveItemFilter(item_filter, items);
);

giveItemFilterFromItemList(item_filter, item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str('The %s item list is empty', item_list)));

    giveItemFilter(item_filter, items);
);

giveItemFilterFromItemLayout(item_filter, item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str('The item layout %s doesn\'t exist', item_layout)));

    items = map(read_file(item_layout_path, 'shared_text'), [lower(_), null]);
    if(!items, _error(str('The %s item layout is empty', item_layout)));

    giveItemFilter(item_filter, items);
);

giveItemFilter(item_filter, items) -> (
    if(global_item_filters~item_filter == null, _error(str('The %s item filter doesn\'t exist', item_filter)));
    if(!items, _error('No items were provided'));

    invalid_items = filter(map(items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str('Invalid items: %s', join(', ', sort(_removeDuplicates(invalid_items))))));
    unstackable_items = filter(map(items, _:0), stack_limit(_) == 1);
    if(unstackable_items, _error(str('You can\'t use unstackables: %s', join(', ', sort(_removeDuplicates(unstackable_items))))));

    hopper_nbt = {
        'BlockEntityTag' -> {
            'Items' -> map(items, [item, nbt] = _; {'Slot' -> _i, 'id' -> item, 'Count' -> 1, ...if(nbt, {'tag' -> nbt(nbt)}, {})})
        },
        'stx' -> {
            'item_filter' -> {
                'item_filter' -> item_filter,
                'items' -> map(items, [item, nbt] = _; {'item' -> item, ...if(nbt, {'nbt' -> nbt}, {})})
            }
        },
        'display' -> {
            'Name' -> str('{"text":"STX %s Item Filter","color":"%s","bold":true,"italic":false}', global_item_filters:item_filter || item_filter, global_color),
            'Lore' -> [str('[{"text":"» ","color":"dark_gray","italic":false},{"text":"%d","color":"%s","italic":false},{"text":" item%s","color":"gray","italic":false}]', length(items), global_color, if(length(items) == 1, '', 's'))]
        },
        'Enchantments' -> [{}]
    };
    run(str('give %s hopper%s', player()~'command_name', encode_nbt(hopper_nbt, true)));

    print(format('f » ', 'g You were given a ', str('%s %s', global_color, global_item_filters:item_filter || item_filter), str('g  item filter with %d item%s', length(items), if(length(items) == 1, '', 's'))));
);

__on_item_filter_placed(block, player_facing, data) -> (
    origin_pos = pos(block);

    hopper_facing = block_state(block, 'facing');
    item_filter = data:'item_filter';
    items = map(parse_nbt(data:'items'), [_:'item', _:'nbt']);

    _handleInvalidItems(items);
    _handleUnstackableItems(items);
    if(global_invalid_items_mode == 'skip', items = filter(items, !_isInvalidItem(_:0) && stack_limit(_:0) != 1));

    fallback_item = if(
        global_invalid_items_mode == 'dummy_item', global_stackable_dummy_item,
        global_invalid_items_mode == 'air', ['air', null]
    );

    placed_blocks = for(items,
        pos = pos_offset(origin_pos, player_facing, _i * global_placing_offset);
        if(!global_replace_blocks && _i != 0 && !air(pos), continue());

        set(pos, 'hopper', {'facing' -> hopper_facing}, {'CustomName' -> null});
    
        [item, nbt] = items:_i;
        
        if(item == 'air' && global_air_mode == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item) || stack_limit(item) == 1,
            if(!fallback_item, continue());
            [item, nbt] = fallback_item;
        );

        _itemFilter(block(pos), item_filter, [item, nbt]);
        _updateComparators(pos);
    );

    print(format('f » ', 'g Placed and filled ', str('%s %s', global_color, placed_blocks), str('g  hopper%s with the ', if(placed_blocks == 1, '', 's')), str('%s %s', global_color, global_item_filters:item_filter), 'g  item filter'));
    sound('entity.evoker.cast_spell', origin_pos);
);

viewItemFilter(item_filter, item_tuple) -> (
    if(global_item_filters~item_filter == null, _error(str('The %s item filter doesn\'t exist', item_filter)));

    [item, count, nbt] = item_tuple || ['fern', null, null];
    if(stack_limit(item) == 1, _error('You can\'t use unstackables'));

    screen = create_screen(player(), 'hopper', str('%s Item Filter', global_item_filters:item_filter || item_filter), _(screen, player, action, data) -> (
        if(action == 'pickup_all' || action == 'quick_move' || (action != 'clone' && data:'slot' < 5), return('cancel'));
    ));
    _itemFilter(screen, item_filter, [item, nbt]);
);

// CONTAINER FILL

_container(block, mode, item_tuple) -> (
    if(
        mode == 'full', _full(block, item_tuple),
        mode == 'full_boxes', _fullBoxes(block, item_tuple),
        mode == 'prefill', _prefill(block, item_tuple),
        mode == 'prefill_unstackables', _prefillUnstackables(block, item_tuple),
        mode == 'single_item', _singleItem(block, item_tuple),
        mode == 'single_stack', _singleStack(block, item_tuple),
    );
);

_emptyBoxes(block, count) -> (
    shulker_box = if(global_shulker_box_color == 'default', 'shulker_box', str('%s_shulker_box', global_shulker_box_color));
    loop(inventory_size(block), inventory_set(block, _, count || 1, shulker_box));

    return(block);
);

_full(block, item_tuple) -> (
    [item, nbt] = item_tuple;

    loop(inventory_size(block), inventory_set(block, _, stack_limit(item), item, nbt));

    return(block);
);

_fullBoxes(block, item_tuple) -> (
    [item, nbt] = item_tuple;
    
    shulker_box = if(global_shulker_box_color == 'default', 'shulker_box', str('%s_shulker_box', global_shulker_box_color));
    loop(inventory_size(block), inventory_set(block, _, 1, shulker_box, _generateFullBoxNbt(item, nbt)));
);

_prefill(block, item_tuple) -> (
    [item, nbt] = item_tuple;
    
    inventory_set(block, 0, stack_limit(item), item, nbt);
    for(range(1, inventory_size(block)), inventory_set(block, _, 1, ...global_stackable_dummy_item));

    return(block);
);

_prefillUnstackables(block, item_tuple) -> (
    [item, nbt] = item_tuple;
    
    inventory_set(block, 0, stack_limit(item), item, nbt);
    for(range(1, inventory_size(block)), inventory_set(block, _, 1, ...global_unstackable_dummy_item));

    return(block);
);

_singleItem(block, item_tuple) -> (
    [item, nbt] = item_tuple;

    inventory_set(block, 0, 1, item, nbt);
    for(range(1, inventory_size(block)), inventory_set(block, _, 0));

    return(block);
);

_singleStack(block, item_tuple) -> (
    [item, nbt] = item_tuple;

    inventory_set(block, 0, stack_limit(item), item, nbt);
    for(range(1, inventory_size(block)), inventory_set(block, _, 0));

    return(block);
);

fillContainersFromItems(from_pos, to_pos, item_string, mode) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    fillContainers(mode, items, from_pos, to_pos);
);

fillContainersFromItemList(from_pos, to_pos, item_list, mode) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str('The %s item list is empty', item_list)));
    
    fillContainers(mode, items, from_pos, to_pos);
);

fillContainersFromItemLayout(from_pos, to_pos, item_layout, mode) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str('The item layout %s doesn\'t exist', item_layout)));

    items = map(read_file(item_layout_path, 'shared_text'), [lower(_), null]);
    if(!items, _error(str('The %s item layout is empty', item_layout)));

    fillContainers(mode, items, from_pos, to_pos);
);

fillContainers(mode, items, from_pos, to_pos) -> (
    if(global_container_modes~mode == null, _error('Invalid container mode'));
    if(!items, _error('No items were provided'));

    _handleInvalidItems(items);
    if(global_invalid_items_mode == 'skip', items = filter(items, !_isInvalidItem(_:0)));

    fallback_item = if(
        global_invalid_items_mode == 'dummy_item', global_stackable_dummy_item,
        global_invalid_items_mode == 'air', ['air', null]
    );

    i = 0;
    affected_blocks = 0;
    for(_scanStripes(from_pos, to_pos),      
        [item, nbt] = if(
            global_extra_containers_mode == 'repeat' || length(items) == 1 || i < length(items), items:i,
            global_extra_containers_mode == 'dummy_item', global_stackable_dummy_item,
            global_extra_containers_mode == 'air', ['air', null],
            global_extra_containers_mode == 'no_fill', continue()
        );

        if(item == 'air' && global_air_mode == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item),
            if(!fallback_item,
                i += 1;
                continue();
            );
            [item, nbt] = fallback_item;
        );

        success_count = for(_,
            block = _;

            if(inventory_has_items(block) != null,
                _container(block, mode, [item, nbt]);
                _updateComparators(block);
                affected_blocks += 1;
            );
        );

        if(!global_skip_empty_spots || success_count > 0, i += 1);
    );

    print(format([
        'f » ',
        str('g %s ', if(
            mode == 'full' || mode == 'full_boxes', 'Completely filled',
            mode == 'prefill' || mode == 'prefill_unstackables', 'Prefilled',
            mode == 'single_item' || mode == 'single_stack', 'Filled'
        )),
        str('%s %s', global_color, affected_blocks),
        str('g  container%s %s', if(affected_blocks == 1, '', 's'), if(
            mode == 'full_boxes', 'with full boxes',
            mode == 'prefill_unstackables', 'with unstackables',
            mode == 'full' || mode == 'prefill', '',
            mode == 'single_item', 'with single items',
            mode == 'single_stack', 'with single stacks'
        ))
    ]));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

giveContainerFromItems(container, item_string, mode) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    giveContainer(mode, container, items);
);

giveContainerFromItemList(container, item_list, mode) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str('The %s item list is empty', item_list)));
    
    giveContainer(mode, container, items);
);

giveContainerFromItemLayout(container, item_layout, mode) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str('The item layout %s doesn\'t exist', item_layout)));

    items = map(read_file(item_layout_path, 'shared_text'), [lower(_), null]);
    if(!items, _error(str('The %s item layout is empty', item_layout)));

    giveContainer(mode, container, items);
);

giveContainer(mode, container, items) -> (
    if(global_container_modes~mode == null, _error('Invalid container mode'));
    if(global_containers~container == null, _error('Invalid container type'));
    if(!items, _error('No items were provided'));

    invalid_items = filter(map(items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str('Invalid items: %s', join(', ', sort(_removeDuplicates(invalid_items))))));

    container_nbt = {
        'BlockEntityTag' -> {
            'Items' -> map(items, [item, nbt] = _; {'Slot' -> _i, 'id' -> item, 'Count' -> 1, ...if(nbt, {'tag' -> nbt(nbt)}, {})})
        },
        'stx' -> {
            'container' -> {
                'mode' -> mode,
                ...if(container == 'double_chest', {'double_chest' -> true}, {}),
                'items' -> map(items, [item, nbt] = _; {'item' -> item, ...if(nbt, {'nbt' -> nbt}, {})})
            }
        },
        'display' -> {
            'Name' -> str('{"text":"STX %s Container","color":"%s","bold":true,"italic":false}', global_container_modes:mode || mode, global_color),
            'Lore' -> [str('[{"text":"» ","color":"dark_gray","italic":false},{"text":"%d","color":"%s","italic":false},{"text":" item%s","color":"gray","italic":false}]', length(items), global_color, if(length(items) == 1, '', 's'))]
        },
        'Enchantments' -> [{}]
    };

    run(str('give %s %s%s', player()~'command_name', if(container == 'double_chest', 'chest', container), encode_nbt(container_nbt, true)));

    print(format('f » ', 'g You were given a ', str('%s %s', global_color, global_container_modes:mode || mode), str('g  container with %d item%s', length(items), if(length(items) == 1, '', 's'))));
);

__on_container_placed(block, player_facing, data) -> (
    origin_pos = pos(block);

    container_facing = block_state(block, 'facing');
    mode = data:'mode';
    items = map(parse_nbt(data:'items'), [_:'item', _:'nbt']);

    _handleInvalidItems(items);
    if(global_invalid_items_mode == 'skip', items = filter(items, !_isInvalidItem(_:0)));

    fallback_item = if(
        global_invalid_items_mode == 'dummy_item', global_stackable_dummy_item,
        global_invalid_items_mode == 'air', ['air', null]
    );

    if(block == 'chest' && data:'double_chest',
        second_chest_offset = global_cardinal_directions:(global_cardinal_directions~container_facing + if(global_double_chest_direction == 'left', 1, -1));
        second_chest_direction = if(global_double_chest_direction == 'left', 'right', 'left');
    );

    placed_blocks = for(items,
        pos = pos_offset(origin_pos, player_facing, _i * global_placing_offset);
        if(!global_replace_blocks && _i != 0 && !air(pos), continue());

        set(pos, str(block), if(container_facing, {'facing' -> container_facing}), {'CustomName' -> null});

        if(block == 'chest' && data:'double_chest',
            pos1 = pos_offset(pos, second_chest_offset);
            if(global_replace_blocks || air(pos1), set(pos1, 'chest', {'facing' -> container_facing, 'type' -> second_chest_direction}, {'CustomName' -> null}));
        );
    
        [item, nbt] = items:_i;
        
        if(item == 'air' && global_air_mode == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item),
            if(!fallback_item, continue());
            [item, nbt] = fallback_item;
        );

        _container(block(pos), mode, [item, nbt]);
        _updateComparators(pos);
    );

    print(format('f » ', str('g Placed and filled %d ', placed_blocks), str('%s %s', global_color, global_container_modes:mode || mode), str('g  container%s', if(placed_blocks == 1, '', 's'))));
    sound('entity.evoker.cast_spell', origin_pos);
);

fillContainersEmptyBoxes(from_pos, to_pos, count) -> (
    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error('You are not looking at any block'));
        from_pos = trace;
    );
    if(!to_pos,
        if(inventory_has_items(from_pos) == null, _error('That block is not a container'));
        to_pos = from_pos;
    );

    affected_blocks = volume(from_pos, to_pos,
        block = _;
        if(inventory_has_items(block) != null,
            _emptyBoxes(block, count);
            _updateComparators(block);
        );
    );

    print(format('f » ', 'g Filled ', str('%s %s', global_color, affected_blocks), str('g  container%s with %sempty boxes', if(affected_blocks == 1, '', 's'), if(count == 1, '', 'stacked '))));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

giveEmptyBoxesContainer(count) -> (
    shulker_box = if(global_shulker_box_color == 'default', 'shulker_box', str('%s_shulker_box', global_shulker_box_color));
    chest_nbt = {
        'BlockEntityTag' -> {
            'Items' -> map(range(27), {'Slot' -> _, 'id' -> shulker_box, 'Count' -> count})
        }
    };

    run(str('give %s chest%s', player()~'command_name', chest_nbt));

    print(format('f » ', str('g You were given a chest with %sempty boxes', if(count == 1, '', 'stacked '))));
);

// CONTAINER EMPTY

_empty(block) -> (
    loop(inventory_size(block), inventory_set(block, _, 0));

    return(block);
);

fillContainersEmpty(from_pos, to_pos) -> (
    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error('You are not looking at any block'));
        from_pos = trace;
    );
    if(!to_pos,
        if(inventory_has_items(from_pos) == null, _error('That block is not a container'));
        to_pos = from_pos;
    );

    affected_blocks = volume(from_pos, to_pos,
        block = _;
        if(inventory_has_items(block) != null,
            _empty(block);
            _updateComparators(block);
        );
    );

    print(format('f » ', 'g Emptied ', str('%s %s', global_color, affected_blocks), str('g  container%s', if(affected_blocks == 1, '', 's'))));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

// MIS CHEST

_MISChest(chest, items) -> (
    fallback_item = if(
        global_invalid_items_mode == 'dummy_item', global_stackable_dummy_item,
        global_invalid_items_mode == 'air', ['air', null]
    );

    loop(inventory_size(chest),
        if(_ >= length(items),
            inventory_set(chest, _, 1, ...global_unstackable_dummy_item);
            continue();
        );

        [item, nbt] = items:_;

        if(item == 'air' && global_air_mode == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item) || stack_limit(item) == 1,
            if(!fallback_item, continue());
            [item, nbt] = fallback_item;
        );
        
        inventory_set(chest, _, 2, item, nbt);
    );

    return(chest);
);

fillMISChests(from_pos, to_pos, item_list_string) -> (
    if(length(filter(to_pos - from_pos, _ == 0)) < 2, _error('The area must be a row of blocks'));

    item_lists = split(' ', item_list_string);
    if(!item_lists, _error('No item lists were provided'));

    invalid_item_lists = filter(item_lists, list_files('item_lists', 'shared_text')~str('item_lists/%s', _) == null);
    if(invalid_item_lists, _error(str('Invalid item lists: %s', join(', ', sort(_removeDuplicates(invalid_item_lists))))));

    item_list_contents = map(item_lists, _readItemList(_));
    all_items = reduce(item_list_contents, [..._a, ..._], []);

    _handleInvalidItems(all_items);
    _handleUnstackableItems(all_items);

    i = 0;
    affected_blocks = for(_scanStrip(from_pos, to_pos),
        block = _;

        if(block != 'chest',
            if(!global_skip_empty_spots, i += 1);
            continue();
        );
        if(i >= length(item_lists), continue());

        items = item_list_contents:i;
        if(global_invalid_items_mode == 'skip', items = filter(items, !_isInvalidItem(_:0) && stack_limit(_:0) != 1));

        i += 1;

        _MISChest(block, items);
    );

    print(format('f » ', 'g Filled ', str('%s %s', global_color, affected_blocks), str('g  MIS Chest%s', if(affected_blocks == 1, '', 's'))));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

giveMISChest(item_list_string) -> (
    item_lists = split(' ', item_list_string);
    if(!item_lists, _error('No item lists were provided'));

    invalid_item_lists = filter(item_lists, list_files('item_lists', 'shared_text')~str('item_lists/%s', _) == null);
    if(invalid_item_lists, _error(str('Invalid item lists: %s', join(', ', sort(_removeDuplicates(invalid_item_lists))))));

    item_list_contents = map(item_lists, _readItemList(_));
    all_items = reduce(item_list_contents, [..._a, ..._], []);

    invalid_items = filter(map(all_items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str('Invalid items: %s', join(', ', sort(_removeDuplicates(invalid_items))))));
    unstackable_items = filter(map(all_items, _:0), stack_limit(_) == 1);
    if(unstackable_items, _error(str('You can\'t use unstackables: %s', join(', ', sort(_removeDuplicates(unstackable_items))))));

    chest_nbt = {
        'stx' -> {
            'mis_chest' -> {
                'items' -> map(item_list_contents, map(_, [item, nbt] = _; {'item' -> item, ...if(nbt, {'nbt' -> nbt}, {})}))
            }
        },
        'display' -> {
            'Name' -> str('{"text":"STX MIS Chest","color":"%s","bold":true,"italic":false}', global_color),
            'Lore' -> [str('[{"text":"» ","color":"dark_gray","italic":false},{"text":"%d","color":"%s","italic":false},{"text":" entr%s","color":"gray","italic":false}]', length(item_lists), global_color, if(length(item_lists) == 1, 'y', 'ies'))]
        },
        'Enchantments' -> [{}]
    };
    run(str('give %s chest%s', player()~'command_name', encode_nbt(chest_nbt, true)));

    print(format('f » ', 'g You were given a ', str('%s MIS Chest', global_color), str('g  with %d entr%s', length(item_lists), if(length(item_lists) == 1, 'y', 'ies'))));
);

__on_MIS_chest_placed(block, player_facing, data) -> (
    origin_pos = pos(block);

    chest_facing = block_state(block, 'facing');
    chest_contents = map(parse_nbt(data:'items'), map(_, [_:'item', _:'nbt']));
    all_items = reduce(chest_contents, [..._a, ..._], []);

    _handleInvalidItems(all_items);
    _handleUnstackableItems(all_items);

    second_chest_offset = global_cardinal_directions:(global_cardinal_directions~chest_facing + if(global_double_chest_direction == 'left', 1, -1));
    second_chest_direction = if(global_double_chest_direction == 'left', 'right', 'left');

    placed_blocks = for(chest_contents,
        pos = pos_offset(origin_pos, player_facing, _i * global_placing_offset);
        pos1 = pos_offset(pos, second_chest_offset);
        if(!global_replace_blocks && _i != 0 && !air(pos), continue());

        set(pos, 'chest', {'facing' -> chest_facing}, {'CustomName' -> null});
        if(global_replace_blocks || air(pos1), set(pos1, 'chest', {'facing' -> chest_facing, 'type' -> second_chest_direction}, {'CustomName' -> null}));
    
        items = chest_contents:_i;
        if(global_invalid_items_mode == 'skip', items = filter(items, !_isInvalidItem(_:0) && stack_limit(_:0) != 1));

        _MISChest(block(pos), items);
    );

    print(format('f » ', 'g Placed and filled ', str('%s %s', global_color, placed_blocks), str('g  MIS Chest%s', if(placed_blocks == 1, '', 's'))));
    sound('entity.evoker.cast_spell', origin_pos);
);

// ENCODER CHEST

_encoderChest(chest, items, signal_strength) -> (
    fallback_item = if(
        global_invalid_items_mode == 'dummy_item', global_stackable_dummy_item,
        global_invalid_items_mode == 'air', ['air', null]
    );

    amount = ceil(inventory_size(chest) * 64 / 14 * signal_strength);

    loop(inventory_size(chest),
        if(inventory_size(chest) - _ - 1 < amount / 64,
            dummy_amount = min(amount, 64);
            [dummy_item, dummy_nbt] = global_stackable_dummy_item;
            inventory_set(chest, _, dummy_amount / (64 / stack_limit(dummy_item)), dummy_item, dummy_nbt);
            amount += -dummy_amount;
            continue();
        );

        [item, nbt] = if(_ < length(items), items:_, global_stackable_dummy_item);

        if(item == 'air' && global_air_mode == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item) || stack_limit(item) == 1,
            if(!fallback_item, continue());
            [item, nbt] = fallback_item;
        );
        
        inventory_set(chest, _, 2, item, nbt);

        if(item != 'air', amount += -2 * (64 / stack_limit(item)));
    );

    return(chest);
);

fillEncoderChests(signal_strength, from_pos, to_pos, item_list_string) -> (
    if(length(filter(to_pos - from_pos, _ == 0)) < 2, _error('The area must be a row of blocks'));

    item_lists = split(' ', item_list_string);
    if(!item_lists, _error('No item lists were provided'));

    invalid_item_lists = filter(item_lists, list_files('item_lists', 'shared_text')~str('item_lists/%s', _) == null);
    if(invalid_item_lists, _error(str('Invalid item lists: %s', join(', ', sort(_removeDuplicates(invalid_item_lists))))));

    item_list_contents = map(item_lists, _readItemList(_));
    all_items = reduce(item_list_contents, [..._a, ..._], []);

    _handleInvalidItems(all_items);
    _handleUnstackableItems(all_items);

    i = 0;
    affected_blocks = for(_scanStrip(from_pos, to_pos),
        block = _;

        if(block != 'chest',
            if(!global_skip_empty_spots, i += 1);
            continue();
        );
        if(i >= length(item_lists), continue());

        items = item_list_contents:i;
        if(global_invalid_items_mode == 'skip', items = filter(items, !_isInvalidItem(_:0) && stack_limit(_:0) != 1));

        i += 1;

        _encoderChest(block, items, signal_strength);
    );

    print(format('f » ', 'g Filled ', str('%s %s', global_color, affected_blocks), str('g  Encoder Chest%s', if(affected_blocks == 1, '', 's'))));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

giveEncoderChest(signal_strength, item_list_string) -> (
    item_lists = split(' ', item_list_string);
    if(!item_lists, _error('No item lists were provided'));

    invalid_item_lists = filter(item_lists, list_files('item_lists', 'shared_text')~str('item_lists/%s', _) == null);
    if(invalid_item_lists, _error(str('Invalid item lists: %s', join(', ', sort(_removeDuplicates(invalid_item_lists))))));

    item_list_contents = map(item_lists, _readItemList(_));
    all_items = reduce(item_list_contents, [..._a, ..._], []);

    invalid_items = filter(map(all_items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str('Invalid items: %s', join(', ', sort(_removeDuplicates(invalid_items))))));
    unstackable_items = filter(map(all_items, _:0), stack_limit(_) == 1);
    if(unstackable_items, _error(str('You can\'t use unstackables: %s', join(', ', sort(_removeDuplicates(unstackable_items))))));

    chest_nbt = {
        'stx' -> {
            'encoder_chest' -> {
                'signal_strength' -> signal_strength,
                'items' -> map(item_list_contents, map(_, [item, nbt] = _; {'item' -> item, ...if(nbt, {'nbt' -> nbt}, {})}))
            }
        },
        'display' -> {
            'Name' -> str('{"text":"STX Encoder Chest","color":"%s","bold":true,"italic":false}', global_color),
            'Lore' -> [str('[{"text":"» ","color":"dark_gray","italic":false},{"text":"Signal strength ","color":"gray","italic":false},{"text":"%d","color":"%s","italic":false}]', signal_strength, global_color), str('[{"text":"» ","color":"dark_gray","italic":false},{"text":"%d","color":"%s","italic":false},{"text":" entr%s","color":"gray","italic":false}]', length(item_lists), global_color, if(length(item_lists) == 1, 'y', 'ies'))]
        },
        'Enchantments' -> [{}]
    };
    run(str('give %s chest%s', player()~'command_name', encode_nbt(chest_nbt, true)));

    print(format('f » ', 'g You were given an ', str('%s Encoder Chest', global_color), str('g  with %d entr%s', length(item_lists), if(length(item_lists) == 1, 'y', 'ies'))));
);

__on_encoder_chest_placed(block, player_facing, data) -> (
    origin_pos = pos(block);

    chest_facing = block_state(block, 'facing');
    signal_strength = data:'signal_strength';
    chest_contents = map(parse_nbt(data:'items'), map(_, [_:'item', _:'nbt']));
    all_items = reduce(chest_contents, [..._a, ..._], []);

    _handleInvalidItems(all_items);
    _handleUnstackableItems(all_items);

    second_chest_offset = global_cardinal_directions:(global_cardinal_directions~chest_facing + if(global_double_chest_direction == 'left', 1, -1));
    second_chest_direction = if(global_double_chest_direction == 'left', 'right', 'left');

    placed_blocks = for(chest_contents,
        pos = pos_offset(origin_pos, player_facing, _i * global_placing_offset);
        pos1 = pos_offset(pos, second_chest_offset);
        if(!global_replace_blocks && _i != 0 && !air(pos), continue());

        set(pos, 'chest', {'facing' -> chest_facing}, {'CustomName' -> null});
        if(global_replace_blocks || air(pos1), set(pos1, 'chest', {'facing' -> chest_facing, 'type' -> second_chest_direction}, {'CustomName' -> null}));
    
        items = chest_contents:_i;
        if(global_invalid_items_mode == 'skip', items = filter(items, !_isInvalidItem(_:0) && stack_limit(_:0) != 1));

        _encoderChest(block(pos), items, signal_strength);
    );

    print(format('f » ', 'g Placed and filled ', str('%s %s', global_color, placed_blocks), str('g  Encoder Chest%s', if(placed_blocks == 1, '', 's'))));
    sound('entity.evoker.cast_spell', origin_pos);
);

// ITEM FRAMES

placeItemFramesFromItems(from_pos, to_pos, facing, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    placeItemFrames(items, from_pos, to_pos, facing);
);

placeItemFramesFromItemList(from_pos, to_pos, facing, item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str('The %s item list is empty', item_list)));

    placeItemFrames(items, from_pos, to_pos, facing);
);

placeItemFramesFromItemLayout(from_pos, to_pos, facing, item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str('The item layout %s doesn\'t exist', item_layout)));

    items = map(read_file(item_layout_path, 'shared_text'), [lower(_), null]);
    if(!items, _error(str('The %s item layout is empty', item_layout)));

    placeItemFrames(items, from_pos, to_pos, facing);
);

placeItemFrames(items, from_pos, to_pos, item_frame_facing) -> (
    if(!items, _error('No items were provided'));
    if(length(filter(to_pos - from_pos, _ == 0)) < 2, _error('The area must be a row of blocks'));

    from_pos = pos_offset(from_pos, item_frame_facing);
    to_pos = pos_offset(to_pos, item_frame_facing);

    _handleInvalidItems(items);
    if(global_invalid_items_mode == 'skip', items = filter(items, !_isInvalidItem(_:0)));

    fallback_item = if(
        global_invalid_items_mode == 'dummy_item', global_stackable_dummy_item,
        global_invalid_items_mode == 'air', ['air', null]
    );

    i = 0;
    spawned_entities = for(_scanStrip(from_pos, to_pos),

        [item, nbt] = if(
            global_extra_containers_mode == 'repeat' || length(items) == 1 || i < length(items), items:i,
            global_extra_containers_mode == 'dummy_item', global_stackable_dummy_item,
            global_extra_containers_mode == 'air', ['air', null],
            global_extra_containers_mode == 'no_fill', continue()
        );
            
        if(item == 'air' && global_air_mode == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item),
            if(!fallback_item,
                i += 1;
                continue();
            );
            [item, nbt] = fallback_item;
        );

        spawn(global_item_frame_type, _, {'Facing' -> global_directions~item_frame_facing, 'Item' -> {'id' -> item, 'Count' -> 1, 'tag' -> nbt}});

        i += 1;
    );

    print(format('f » ', 'g Placed ', str('%s %s', global_color, spawned_entities), str('g  item frame%s', if(spawned_entities == 1, '', 's'))));
    run(str('playsound block.note_block.pling master %s', player()~'command_name'));
);

giveItemFramesFromItems(item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    giveItemFrames(items);
);

giveItemFramesFromItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str('The %s item list is empty', item_list)));

    giveItemFrames(items);
);

giveItemFramesFromItemLayout(item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str('The item layout %s doesn\'t exist', item_layout)));

    items = map(read_file(item_layout_path, 'shared_text'), [lower(_), null]);
    if(!items, _error(str('The %s item layout is empty', item_layout)));

    giveItemFrames(items);
);

giveItemFrames(items) -> (
    if(!items, _error('No items were provided'));

    invalid_items = filter(map(items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str('Invalid items: %s', join(', ', sort(_removeDuplicates(invalid_items))))));

    item_frame_nbt = {
        'BlockEntityTag' -> {
            'Items' -> map(items, [item, nbt] = _; {'Slot' -> _i, 'id' -> item, 'Count' -> 1, ...if(nbt, {'tag' -> nbt(nbt)}, {})})
        },
        'stx' -> {
            'item_frame' -> {
                'items' -> map(items, [item, nbt] = _; {'item' -> item, ...if(nbt, {'nbt' -> nbt}, {})})
            }
        },
        'display' -> {
            'Name' -> str('{"text":"STX Item Frame","color":"%s","bold":true,"italic":false}', global_color),
            'Lore' -> [str('[{"text":"» ","color":"dark_gray","italic":false},{"text":"%d","color":"%s","italic":false},{"text":" items","color":"gray","italic":false}]', length(items), global_color)]
        },
        'Enchantments' -> [{}]
    };
    run(str('give %s %s%s', player()~'command_name', global_item_frame_type, encode_nbt(item_frame_nbt, true)));

    print(format('f » ', str('g You were given an item frame with %d item%s', length(items), if(length(items) == 1, '', 's'))));
);

__on_item_frame_placed(item_frame_type, origin_pos, item_frame_facing, player_facing, data) -> (
    items = map(parse_nbt(data:'items'), [_:'item', _:'nbt']);

    _handleInvalidItems(items);
    if(global_invalid_items_mode == 'skip', items = filter(items, !_isInvalidItem(_:0)));

    fallback_item = if(
        global_invalid_items_mode == 'dummy_item', global_stackable_dummy_item,
        global_invalid_items_mode == 'air', ['air', null]
    );

    spawned_item_frames = for(items,
        pos = pos_offset(origin_pos, player_facing, _i * global_placing_offset);

        [item, nbt] = items:_i;
        
        if(item == 'air' && global_air_mode == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item),
            if(!fallback_item, continue());
            [item, nbt] = fallback_item;
        );

        spawn(item_frame_type, pos, {'Facing' -> global_directions~item_frame_facing, 'Item' -> {'id' -> item, 'Count' -> 1, 'tag' -> nbt}});
    );

    print(format('f » ', 'g Placed ', str('%s %s', global_color, spawned_item_frames), str('g  item frame%s', if(spawned_item_frames == 1, '', 's'))));
    sound('entity.evoker.cast_spell', origin_pos);
);

// CHESTS

giveChestsFromItems(item_string, mode) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    giveChests(items, mode);
);

giveChestsFromItemList(item_list, mode) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str('The %s item list is empty', item_list)));

    giveChests(items, mode);    
);

giveChestsFromItemLayout(item_layout, mode) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str('The item layout %s doesn\'t exist', item_layout)));

    items = map(read_file(item_layout_path, 'shared_text'), [lower(_), null]);
    if(!items, _error(str('The %s item layout is empty', item_layout)));

    giveChests(items, mode);
);

giveChests(items, mode) -> (
    if(!items, _error('No items were provided'));

    invalid_items = filter(map(items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str('Invalid items: %s', join(', ', sort(_removeDuplicates(invalid_items))))));

    shulker_box = if(global_shulker_box_color == 'default', 'shulker_box', str('%s_shulker_box', global_shulker_box_color));
    chest_amount = ceil(length(items) / 27);

    loop(chest_amount,
        item_group = slice(items, _ * 27, min(length(items), (_ + 1) * 27));
        nbt = {
            'BlockEntityTag' -> {
                'Items' -> map(item_group,
                    [item, nbt] = _;
                    if(
                        mode == 'full_boxes',
                            {'Slot' -> _i, 'id' -> shulker_box, 'Count' -> 1, 'tag' -> _generateFullBoxNbt(item, nbt)},
                        mode == 'stacks',
                            {'Slot' -> _i, 'id' -> item, 'Count' -> stack_limit(item), ...if(nbt, {'tag' -> nbt(nbt)}, {})},
                        mode == 'single_items',
                            {'Slot' -> _i, 'id' -> item, 'Count' -> 1, ...if(nbt, {'tag' -> nbt(nbt)}, {})}              
                    );
                );
            },
            'display' -> {
                'Name' -> str('{"text":"%s Chest #%d","color":"%s","bold":true,"italic":false}', global_chest_modes:mode || mode, _ + 1, global_color)
            }
        };
        run(str('give %s chest%s', player()~'command_name', encode_nbt(nbt)));
    );

    print(format('f » ', str('g You were given %d ', chest_amount), str('%s %s', global_color, global_chest_modes:mode || mode), str('g  chest%s', if(chest_amount == 1, '', 's'))));
);

// BUNDLES

getBundleFromItems(item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    getBundle(items);
);

getBundleFromItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str('The item list %s doesn\'t exist', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str('The %s item list is empty', item_list)));

    getBundle(items);    
);

getBundleFromItemLayout(item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str('The item layout %s doesn\'t exist', item_layout)));

    items = map(read_file(item_layout_path, 'shared_text'), [lower(_), null]);
    if(!items, _error(str('The %s item layout is empty', item_layout)));

    getBundle(items);
);

getBundle(items) -> (
    if(!items, _error('No items were provided'));

    invalid_items = filter(map(items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str('Invalid items: %s', join(', ', sort(_removeDuplicates(invalid_items))))));

    bundle_nbt = {'Items' -> map(items, [item, nbt] = _; {'id' -> item, 'Count' -> 1, ...if(nbt, {'tag' -> nbt(nbt)}, {})})};
    run(str('give %s bundle%s', player()~'command_name', encode_nbt(bundle_nbt, true)));
);

// FULL BOXES

getFullBox(item_tuple, shulker_box_color) -> (
    [item, count, nbt] = item_tuple;
    shulker_box = if(shulker_box_color, str('%s_shulker_box', shulker_box_color), global_shulker_box_color == 'default', 'shulker_box', str('%s_shulker_box', global_shulker_box_color));
    shulker_box_nbt = _generateFullBoxNbt(item, nbt);
    run(str('give %s %s%s', player()~'command_name', shulker_box, shulker_box_nbt));
);

// EVENTS

__on_player_places_block(player, item_tuple, hand, block) -> (
    [item, count, nbt] = item_tuple;
    player_facing = _getFacing(player);
    if(
        item == 'hopper' && has(nbt, 'stx.item_filter'),
            data = nbt:'stx':'item_filter';
            __on_item_filter_placed(block, player_facing, data),
        item == 'chest' && has(nbt, 'stx.mis_chest'),
            data = nbt:'stx':'mis_chest';
            __on_MIS_chest_placed(block, player_facing, data),
        item == 'chest' && has(nbt, 'stx.encoder_chest'),
            data = nbt:'stx':'encoder_chest';
            __on_encoder_chest_placed(block, player_facing, data),
        has(nbt, 'stx.container'),
            data = nbt:'stx':'container';
            __on_container_placed(block, player_facing, data);
    );
);

__on_player_right_clicks_block(player, item_tuple, hand, block, face, hitvec) -> (
    if(!item_tuple, return());
    [item, count, nbt] = item_tuple;
    if((item != 'item_frame' && item != 'glow_item_frame') || !has(nbt, 'stx.item_frame'), return());
    item_frame_type = item;
    origin_pos = pos_offset(block, face);
    player_facing = _getFacing(player);
    data = nbt:'stx':'item_frame';
    schedule(0, _(outer(item_frame_type), outer(origin_pos), outer(face), outer(player_facing), outer(data)) -> (
        if(!entity_area(item_frame_type, origin_pos, [0.5, 0.5, 0.5]), return());
        __on_item_frame_placed(item_frame_type, origin_pos, face, player_facing, data);
    ));
);

__on_close() -> (
    data = {
        'stackable_dummy_item' -> global_stackable_dummy_item,
        'unstackable_dummy_item' -> global_unstackable_dummy_item,
        'shulker_box_color' -> global_shulker_box_color,
        'item_frame_type' -> global_item_frame_type,
        'filter_item_amount' -> global_filter_item_amount,
        'double_chest_direction' -> global_double_chest_direction,
        'extra_containers_mode' -> global_extra_containers_mode,
        'invalid_items_mode' -> global_invalid_items_mode,
        'air_mode' -> global_air_mode,
        'placing_offset' -> global_placing_offset,
        'skip_empty_spots' -> global_skip_empty_spots,
        'replace_blocks' -> global_replace_blocks,
        'minimal_filling' -> global_minimal_filling
    };
    write_file('config', 'json', data);
);