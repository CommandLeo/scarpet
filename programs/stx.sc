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
    'single_stack' -> 'Single Stack',
    'single_full_box' -> 'Single Full Box'
};
global_mixed_chest_modes = {
    'single_items' -> 'Single Items',
    'single_stacks' -> 'Item Stacks',
    'single_item_boxes' -> 'Single Item Boxes',
    'single_stack_boxes' -> 'Single Stack Boxes',
    'full_boxes' -> 'Full Boxes',
    'mixed_boxes' -> 'Mixed Boxes'
};
global_default_item_filters = [
    ...if(system_info('game_pack_version') < 33 || system_info('world_carpet_rules'):'stackableShulkerBoxes' == true, ['overstacking'], []),
    'box_sorter',
    'stack_separation',
    'ss3',
    'ssi_ss2'
];
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
global_extra_containers_mode_options = {
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
global_obtainabilities = {
    'everything' -> 'Everything',
    'main_storage' -> 'Main Storage',
    'survival_obtainables' -> 'Survival Obtainables'
};
global_stackabilities = {
    'stackables' -> [16, 64],
    '64_stackables' -> [64],
    '16_stackables' -> [16],
    'unstackables' -> [1]
};
global_error_messages = {
    'ALLITEMS_NOT_INSTALLED' -> 'You must install the getallitems script to use this feature',
    'CANT_USE_UNSTACKABLES' -> 'You can\'t use unstackables: %s',
    'FILE_DELETION_ERROR' -> 'There was an error while deleting the file',
    'FILE_WRITING_ERROR' -> 'There was an error while writing the file',
    'INVALID_AIR_MODE' -> 'Invalid air mode',
    'INVALID_CONTAINER_MODE' -> 'Invalid container mode',
    'INVALID_CONTAINER_TYPE' -> 'Invalid container type',
    'INVALID_DIRECTION' -> 'Invalid direction',
    'INVALID_DOUBLE_CHEST_DIRECTION' -> 'Invalid double chest direction',
    'INVALID_EXTRA_CONTAINERS_MODE' -> 'Invalid extra containers mode',
    'INVALID_INDEX' -> 'Invalid index',
    'INVALID_INVALID_ITEMS_MODE' -> 'Invalid invalid items mode',
    'INVALID_ITEMS' -> 'Invalid items: %s',
    'INVALID_ITEM_AMOUNT' -> 'Invalid item amount',
    'INVALID_ITEM_FILTER' -> 'Invalid item filter',
    'INVALID_ITEM_FRAME_TYPE' -> 'Invalid item frame type',
    'INVALID_ITEM_LAYOUTS' -> 'Invalid item layouts: %s',
    'INVALID_ITEM_LISTS' -> 'Invalid item lists: %s',
    'INVALID_OBTAINABILITY' -> 'Invalid obtainability',
    'INVALID_PAGE_NUMBER' -> 'Invalid page number',
    'INVALID_PLACING_OFFSET' -> 'Invalid placing offset',
    'INVALID_SHULKER_BOX_COLOR' -> 'Invalid shulker box color',
    'INVALID_STACKABILITY' -> 'Invalid stackability',
    'ITEM_LAYOUT_DOESNT_EXIST' -> 'The item layout %s doesn\'t exist',
    'ITEM_LAYOUT_EMPTY' -> 'The %s item layout is empty',
    'ITEM_LIST_ALREADY_EXISTS' -> 'An item list with that name already exists',
    'ITEM_LIST_DOESNT_EXIST' -> 'The item list %s doesn\'t exist',
    'ITEM_LIST_EMPTY' -> 'The %s item list is empty',
    'MODULE_NOT_FOUND' -> 'There is no module with that name',
    'NOT_A_CONTAINER' -> 'That block is not a container',
    'NOT_A_HOPPER' -> 'That block is not a hopper',
    'NOT_A_ROW_OF_BLOCKS' -> 'The area must be a row of blocks',
    'NOT_HOLDING_ANY_ITEM' -> 'You are not holding any item',
    'NOT_LOOKING_AT_ANY_BLOCK' -> 'You are not looking at any block',
    'NO_CONTAINER_FOUND' -> 'No container was found',
    'NO_ENTRIES_TO_REMOVE' -> 'No entries to remove from the item list',
    'NO_ITEMS_FOUND' -> 'No items were found',
    'NO_ITEMS_PROVIDED' -> 'No items were provided',
    'NO_ITEMS_TO_ADD' -> 'No items to add to the item list',
    'NO_ITEM_LAYOUTS_PROVIDED' -> 'No item layouts were provided',
    'NO_ITEM_LISTS' -> 'There are no item lists saved',
    'NO_ITEM_LISTS_PROVIDED' -> 'No item lists were provided',
    'ONLY_STACKABLES_ALLOWED' -> 'You can use only stackable items',
    'ONLY_UNSTACKABLES_ALLOWED' -> 'You can use only unstackable items',
    'SELECTED_AREA_EMPTY' -> 'The selected area is empty',
};

global_current_page = {};

global_help_pages = [
    [
        '%color%b StorageTechX Help\n\n',
        '%color% /%app_name% help', 'f ｜', 'g Shows this help menu', ' \n',
        '%color% /%app_name% modules', 'f ｜', 'g List and install additional apps', ' \n',
        '%color% /%app_name% settings', 'f ｜', 'g Manage the settings', ' \n',
        '%color% /%app_name% item_list', 'f ｜', 'g Manage item lists', ' \n',
        '%color% /%app_name% dummy_item', , 'f ｜', 'g Customise the dummy item', ' \n'
    ],
    [
        '%color% /%app_name% empty', 'f ｜', 'g Empties one or more containers', ' \n',
        '%color% /%app_name% quick_fill', 'f ｜', 'g Fills a container with a single item type', ' \n',
        '%color% /%app_name% item_filter', 'f ｜', 'g Fills multiple item filters with different items', ' \n',
        '%color% /%app_name% container', 'f ｜', 'g Fills multiple containers with different items', ' \n',
        '%color% /%app_name% redye_boxes', 'f ｜', 'g Redyes shulker boxes in containers', ' \n',
        '%color% /%app_name% item_frame', 'f ｜', 'g Places item frames in a row with different items', ' \n',
    ],
    [
        '%color% /%app_name% encoder_chest', 'f ｜', 'g Configures encoder chests', ' \n',
        '%color% /%app_name% mis_chest', 'f ｜', 'g Configures MIS chests', ' \n',
        '%color% /%app_name% mixed_chests', 'f ｜', 'g Gives chests with multiple different items inside', ' \n',
        '%color% /%app_name% bundle', 'f ｜', 'g Gives a bundle with different items', ' \n',
        '%color% /%app_name% full_box', 'f ｜', 'g Gives a box full of one item', ' \n',
        '%color% /%app_name% missing_items', 'f ｜', 'g Shows missing items in an item list', ' \n',
    ]
];

_checkVersion(version) -> (
    regex = '(\\d+)\.(\\d+)\.(\\d+)';
    target_version = map(version~regex, number(_));
    scarpet_version = map(system_info('scarpet_version')~regex, number(_));
    return(scarpet_version >= target_version);
);

__config() -> {
    'commands' -> {
        '' -> 'menu',
        'help' -> ['help', 1],
        'help <page>' -> 'help',

        'modules' -> 'listModules',
        'modules list' -> 'listModules',
        'modules install <module>' -> 'installModule',

        'item_list create <name> items <items>' -> 'createItemListFromItems',
        'item_list create <name> item_layout <item_layout>' -> 'createItemListFromItemLayout',
        'item_list create <name> all_items <obtainability> <stackability>' -> 'createItemListFromAllItems',
        'item_list create <name> all_items <obtainability>' -> ['createItemListFromAllItems', null],
        'item_list create <name> containers' -> ['createItemListFromContainers', null, null],
        'item_list create <name> containers <from_pos>' -> ['createItemListFromContainers', null],
        'item_list create <name> containers <from_pos> <to_pos>' -> 'createItemListFromContainers',
        'item_list create <name> area <from_pos> <to_pos>' -> 'createItemListFromArea',
        'item_list rename <item_list> <name>' -> 'renameItemList',
        'item_list clone <item_list> <name>' -> 'cloneItemList',
        'item_list delete <item_list>' -> ['deleteItemList', false],
        'item_list delete <item_list> confirm' -> ['deleteItemList', true],
        'item_list edit <item_list>' -> 'editItemList',
        'item_list edit <item_list> add items <items>' -> 'addEntriesToItemListFromItems',
        'item_list edit <item_list> add item_list <other_item_list>' -> 'addEntriesToItemListFromItemList',
        'item_list edit <item_list> add item_layout <item_layout>' -> 'addEntriesToItemListFromItemLayout',
        'item_list edit <item_list> add containers' -> ['addEntriesToItemListFromContainers', null, null],
        'item_list edit <item_list> add containers <from_pos>' -> ['addEntriesToItemListFromContainers', null],
        'item_list edit <item_list> add containers <from_pos> <to_pos>' -> 'addEntriesToItemListFromContainers',
        'item_list edit <item_list> add area <from_pos> <to_pos>' -> 'addEntriesToItemListFromArea',
        'item_list edit <item_list> remove items <entries>' -> 'removeEntriesFromItemListFromItems',
        'item_list edit <item_list> remove item_list <other_item_list>' -> 'removeEntriesFromItemListFromItemList',
        'item_list edit <item_list> remove index <index>' -> 'removeEntriesFromItemListWithIndex',
        'item_list list' -> 'listItemLists',
        'item_list info <item_list>' -> 'infoItemList',
        ...if(_checkVersion('1.4.57'), {'item_list view <item_list>' -> 'viewItemList'}, {}),

        'settings' -> 'settings',
        'settings shulker_box_color' -> 'printShulkerBoxColorSetting',
        'settings shulker_box_color <shulker_box_color>' -> 'setShulkerBoxColorSetting',
        'settings shulker_box_color reset' -> ['setShulkerBoxColorSetting', null],
        'settings item_frame_type' -> 'printItemFrameTypeSetting',
        'settings item_frame_type <item_frame_type>' -> 'setItemFrameTypeSetting',
        'settings item_frame_type reset' -> ['setItemFrameTypeSetting', null],
        'settings filter_item_amount' -> 'printFilterItemAmountSetting',
        'settings filter_item_amount <item_amount>' -> 'setFilterItemAmountSetting',
        'settings filter_item_amount reset' -> ['setFilterItemAmountSetting', null],
        'settings double_chest_direction' -> 'printDoubleChestDirectionSetting',
        'settings double_chest_direction <direction>' -> 'setDoubleChestDirectionSetting',
        'settings double_chest_direction reset' -> ['setDoubleChestDirectionSetting', null],
        'settings extra_containers_mode' -> 'printExtraContainersModeSetting',
        'settings extra_containers_mode <extra_containers_mode>' -> 'setExtraContainersModeSetting',
        'settings extra_containers_mode reset' -> ['setExtraContainersModeSetting', null],
        'settings invalid_items_mode' -> 'printInvalidItemsModeSetting',
        'settings invalid_items_mode <invalid_items_mode>' -> 'setInvalidItemsModeSetting',
        'settings invalid_items_mode reset' -> ['setInvalidItemsModeSetting', null],
        'settings air_mode' -> 'printAirModeSetting',
        'settings air_mode <air_mode>' -> 'setAirModeSetting',
        'settings air_mode reset' -> ['setAirModeSetting', null],
        'settings placing_offset' -> 'printPlacingOffsetSetting',
        'settings placing_offset <offset>' -> 'setPlacingOffsetSetting',
        'settings placing_offset reset' -> ['setPlacingOffsetSetting', null],
        'settings skip_empty_spots' -> 'printSkipEmptySpotsSetting',
        'settings skip_empty_spots <bool>' -> 'setSkipEmptySpotsSetting',
        'settings skip_empty_spots reset' -> ['setSkipEmptySpotsSetting', null],
        'settings replace_blocks' -> 'printReplaceBlocksSetting',
        'settings replace_blocks <bool>' -> 'setReplaceBlocksSetting',
        'settings replace_blocks reset' -> ['setReplaceBlocksSetting', null],
        'settings minimal_filling' -> 'printMinimalFillingSetting',
        'settings minimal_filling <bool>' -> 'setMinimalFillingSetting',
        'settings minimal_filling reset' -> ['setMinimalFillingSetting', null],
        'settings prefer_unstackables' -> 'printPreferUnstackables',
        'settings prefer_unstackables <bool>' -> 'setPreferUnstackables',
        'settings prefer_unstackables reset' -> ['setPreferUnstackables', null],
        'settings encoder_chest_max_filling' -> 'printEncoderChestMaxFilling',
        'settings encoder_chest_max_filling <bool>' -> 'setEncoderChestMaxFilling',
        'settings encoder_chest_max_filling reset' -> ['setEncoderChestMaxFilling', null],
        'settings encoder_chest_fill_all_slots' -> 'printEncoderChestFillAllSlots',
        'settings encoder_chest_fill_all_slots <bool>' -> 'setEncoderChestFillAllSlots',
        'settings encoder_chest_fill_all_slots reset' -> ['setEncoderChestFillAllSlots', null],
        'settings encoder_chest_item_amount' -> 'printEncoderChestItemAmount',
        'settings encoder_chest_item_amount <item_amount>' -> 'setEncoderChestItemAmount',
        'settings encoder_chest_item_amount reset' -> ['setEncoderChestItemAmount', null],

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

        ...reduce(global_container_modes,
            {
                ..._a,
                str('container %s fill <from_pos> <to_pos> items <items>', _) -> ['fillContainersFromItems', _],
                str('container %s fill <from_pos> <to_pos> item_list <item_list>', _) -> ['fillContainersFromItemList', _],
                str('container %s fill <from_pos> <to_pos> item_layout <item_layout>', _) -> ['fillContainersFromItemLayout', _],
                str('container %s give <container> items <items>', _) -> ['giveContainerFromItems', _],
                str('container %s give <container> item_list <item_list>', _) -> ['giveContainerFromItemList', _],
                str('container %s give <container> item_layout <item_layout>', _) -> ['giveContainerFromItemLayout', _]
            },
            {}
        ),

        'quick_fill empty_boxes <from_pos> <to_pos>' -> ['quickFillEmptyBoxes', 1],
        'quick_fill empty_boxes <from_pos>' -> ['quickFillEmptyBoxes', null, 1],
        'quick_fill empty_boxes' -> ['quickFillEmptyBoxes', null, null, 1],
        'quick_fill empty_boxes_stacked <from_pos> <to_pos>' -> ['quickFillEmptyBoxes', 64],
        'quick_fill empty_boxes_stacked <from_pos>' -> ['quickFillEmptyBoxes', null, 64],
        'quick_fill empty_boxes_stacked' -> ['quickFillEmptyBoxes', null, null, 64],

        'quick_fill item_filter <item_filter> <item>' -> ['quickFillItemFilter', null, null],
        'quick_fill item_filter <item_filter> <item> <from_pos>' -> ['quickFillItemFilter', null],
        'quick_fill item_filter <item_filter> <item> <from_pos> <to_pos>' -> 'quickFillItemFilter',

        ...reduce(global_container_modes,
            {
                ..._a,
                str('quick_fill %s <item>', _) -> ['quickFillContainer', null, null, _],
                str('quick_fill %s <item> <from_pos>', _) -> ['quickFillContainer', null, _],
                str('quick_fill %s <item> <from_pos> <to_pos>', _) -> ['quickFillContainer', _]
            },
            {}
        ),

        'empty' -> ['fillContainersEmpty', null, null, null],
        'empty <from_pos>' -> ['fillContainersEmpty', null, null],
        'empty <from_pos> <to_pos>' -> ['fillContainersEmpty', null],
        'empty <from_pos> <to_pos> <block>' -> 'fillContainersEmpty',

        'redye_boxes <shulker_box_color>' -> ['redyeBoxes', null, null],
        'redye_boxes <shulker_box_color> <from_pos>' -> ['redyeBoxes', null],
        'redye_boxes <shulker_box_color> <from_pos> <to_pos>' -> 'redyeBoxes',

        ...reduce(global_mixed_chest_modes,
            {
                ..._a,
                str('mixed_chests %s items <items>', _) -> ['giveMixedChestsFromItems', _],
                str('mixed_chests %s item_list <item_list>', _) -> ['giveMixedChestsFromItemList', _],
                str('mixed_chests %s item_layout <item_layout>', _) -> ['giveMixedChestsFromItemLayout', _],
            },
            {}
        ),

        'mis_chest fill <from_pos> <to_pos> <item_lists>' -> 'fillMISChests',
        'mis_chest give <item_lists>' -> 'giveMISChest',

        'encoder_chest <target_signal_strength> fill <from_pos> <to_pos> <item_lists>' -> 'fillEncoderChests',
        'encoder_chest <target_signal_strength> fix' -> ['fixEncoderChests', null, null],
        'encoder_chest <target_signal_strength> fix <from_pos>' -> ['fixEncoderChests', null],
        'encoder_chest <target_signal_strength> fix <from_pos> <to_pos>' -> 'fixEncoderChests',
        'encoder_chest <target_signal_strength> give <item_lists>' -> 'giveEncoderChest',

        'item_frame place <from_pos> <to_pos> <facing> items <items>' -> 'placeItemFramesFromItems',
        'item_frame place <from_pos> <to_pos> <facing> item_list <item_list>' -> 'placeItemFramesFromItemList',
        'item_frame place <from_pos> <to_pos> <facing> item_layout <item_layout>' -> 'placeItemFramesFromItemLayout',
        'item_frame give items <items>' -> 'giveItemFramesFromItems',
        'item_frame give item_list <item_list>' -> 'giveItemFramesFromItemList',
        'item_frame give item_layout <item_layout>' -> 'giveItemFramesFromItemLayout',

        'missing_items <item_list> item_layouts <item_layouts>' -> 'missingItemsFromItemLayouts',
        'missing_items <item_list> item_lists <item_lists>' -> 'missingItemsFromItemLists',

        'full_box <item>' -> ['getFullBox', null],
        'full_box <item> <shulker_box_color>' -> 'getFullBox',

        ...if(system_info('game_data_version') >= 2683, {
            'bundle items <items>' -> 'giveBundleFromItems',
            'bundle item_list <item_list>' -> 'giveBundleFromItemList',
            'bundle item_layout <item_layout>' -> 'giveBundleFromItemLayout'
        }, {})
    },
    'arguments' -> {
        'item' -> {
            'type' -> 'item'
        },
        'block' -> {
            'type' -> 'blockpredicate'
        },
        'page' -> {
            'type' -> 'int',
            'min' -> 1,
            'max' -> length(global_help_pages),
            'suggest' -> [range(length(global_help_pages))] + 1
        },
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
                entries = split(' ', input);
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
                items = split(' ', input);
                if(items && slice(input, -1) != ' ', delete(items, -1));
                return(if(items, map(item_list(), str('%s %s', join(' ', items), _)), item_list()));
            ),
            'case_sensitive' -> false
        },
        'stackable_items' -> {
            'type' -> 'text',
            'suggester' -> _(args) -> (
                input = args:'stackable_items';
                items = split(' ', input);
                stackable_items = filter(item_list(), !_isUnstackable(_));
                if(items && slice(input, -1) != ' ', delete(items, -1));
                return(if(items, map(stackable_items, str('%s %s', join(' ', items), _)), stackable_items));
            ),
            'case_sensitive' -> false
        },
        'item_lists' -> {
            'type' -> 'text',
            'suggester' -> _(args) -> (
                input = args:'item_lists';
                entries = split(' ', input);
                item_lists = map(list_files('item_lists', 'shared_text'), slice(_, length('item_lists') + 1));
                if(entries && slice(input, -1) != ' ', delete(entries, -1));
                return(if(entries, map(item_lists, str('%s %s', join(' ', entries), _)), item_lists));
            ),
            'case_sensitive' -> false
        },
        'item_layouts' -> {
            'type' -> 'text',
            'suggester' -> _(args) -> (
                input = args:'item_layouts';
                entries = split(' ', input);
                item_layouts = map(list_files('item_layouts', 'shared_text'), slice(_, length('item_layouts') + 1));
                if(entries && slice(input, -1) != ' ', delete(entries, -1));
                return(if(entries, map(item_layouts, str('%s %s', join(' ', entries), _)), item_layouts));
            ),
            'case_sensitive' -> false
        },
        'item_filter' -> {
            'type' -> 'term',
            'suggest' -> global_default_item_filters,
            'case_sensitive' -> false
        },
        'item_layout' -> {
            'type' -> 'term',
            'suggester' -> _(args) -> map(list_files('item_layouts', 'shared_text'), slice(_, length('item_layouts') + 1)),
            'case_sensitive' -> false
        },
        'module' -> {
            'type' -> 'term',
            'suggest' -> map(global_modules, _:'name')
        },
        'container' -> {
            'type' -> 'term',
            'suggest' -> global_containers,
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
        'obtainability' -> {
            'type' -> 'term',
            'suggest' -> keys(global_obtainabilities)
        },
        'stackability' -> {
            'type' -> 'term',
            'suggest' -> keys(global_stackabilities)
        },
        'direction' -> {
            'type' -> 'term',
            'suggest' -> keys(global_double_chest_direction_options),
            'case_sensitive' -> false
        },
        'target_signal_strength' -> {
            'type' -> 'int',
            'min' -> 1,
            'max' -> 13,
            'suggest' -> [1, 2, 3, 4],
        },
        'item_amount' -> {
            'type' -> 'int',
            'min' -> 1,
            'max' -> 63,
            'suggest' -> [1, 2, 3]
        },
        'shulker_box_color' -> {
            'type' -> 'term',
            'suggest' -> keys(global_shulker_box_colors),
            'case_sensitive' -> false
        },
        'item_frame_type' -> {
            'type' -> 'term',
            'suggest' -> keys(global_item_frame_types),
            'case_sensitive' -> false
        },
        'offset' -> {
            'type' -> 'int',
            'min' -> 1,
            'suggest' -> [1, 2]
        },
        'extra_containers_mode' -> {
            'type' -> 'term',
            'suggest' -> keys(global_extra_containers_mode_options),
            'case_sensitive' -> false
        },
        'invalid_items_mode' -> {
            'type' -> 'term',
            'suggest' -> keys(global_invalid_items_mode_options),
            'case_sensitive' -> false
        },
        'air_mode' -> {
            'type' -> 'term',
            'suggest' -> keys(global_air_mode_options),
            'case_sensitive' -> false
        },
        'facing' -> {
            'type' -> 'term',
            'suggest' -> global_directions,
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
    'scope' -> 'global',
    'strict' -> true
};

// HELPER FUNCTIONS

_error(error) -> (
    print(format(str('r %s', error)));
    run('playsound block.note_block.didgeridoo master @s');
    exit();
);

_removeDuplicates(list) -> filter(list, list~_ == _i);

// UTILITY FUNCTIONS

_parseEntry(entry) -> (
    if(!entry, return([null, null]));

    i = split(entry)~'{';
    [item, nbt] = if(
        i == 0,
            [null, null],
        i != null,
            [lower(slice(entry, 0, i)) || null, parse_nbt(slice(entry, i))],
        // else
            [lower(entry), null]
    );
    return([item, nbt]);
);

_itemToString(item_tuple) -> (
    [item, nbt] = item_tuple;
    return(item + if(nbt, encode_nbt(nbt, true), ''));
);

_readItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    entries = map(read_file(item_list_path, 'shared_text'), _parseEntry(_));
    entries = filter(entries, _:0);
    return(entries);
);

_readItemLayout(item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    entries = map(read_file(item_layout_path, 'shared_text'), _parseEntry(_));
    entries = filter(entries, _:0);
    return(entries);
);

_itemToMap(slot, item, count, nbt) -> (
    if(
        system_info('game_pack_version') >= 33,
            {'slot' -> slot, 'item' -> {'id' -> item, 'count' -> count, if(nbt, 'components' -> nbt, ...{})}},
        // else
            {'Slot' -> slot, 'id' -> item, 'Count' -> count, if(nbt, 'tag' -> nbt, ...{})}
    );
);

_formatTextComponent(text_component) -> (
    return(
        if(
            system_info('game_pack_version') >= 62,
                text_component,
            // else
                encode_json(text_component)
        );
    );
);

_giveCommand(item, nbt) -> (
    if(type(nbt) == 'nbt', nbt = parse_nbt(nbt));
    command = 'give @s ' + item + if(nbt, if(system_info('game_pack_version') >= 33, '[' + join(',', map(nbt, _ + '=' + encode_nbt(nbt:_, true))) + ']', encode_nbt(nbt, true)), '');
    return(command);
);

_getShulkerBoxString(color) -> (
    return(
        if(
            color == 'default',
                'shulker_box',
            // else
                str('%s_shulker_box', color)
        )
    );
);

_generateFullShulkerBox(item, nbt) -> (
    item_maps = map(range(27), _itemToMap(_i, item, stack_limit(item), nbt));
    return(if(system_info('game_pack_version') >= 33, {'container' -> item_maps}, {'BlockEntityTag' -> {'Items' -> item_maps}}));
);

_isInvalidItem(item) -> (
    return(item_list()~item == null || (item == 'air' && global_settings:'air_mode' == 'invalid'));
);

_handleInvalidItems(item_tuples) -> (
    invalid_items = sort(_removeDuplicates(filter(map(item_tuples, _:0), _isInvalidItem(_))));
    if(invalid_items,
        if(
            global_settings:'invalid_items_mode' == 'error',
                _error(str(global_error_messages:'INVALID_ITEMS', join(', ', invalid_items))),
            global_settings:'invalid_items_mode' == 'skip',
                print(format('f » ', 'rb WARNING!', str('g  Skipped %d invalid items: %s', length(invalid_items), join(', ', invalid_items)))),
            // else
                print(format('f » ', str('g Ignored %d invalid items: %s', length(invalid_items), join(', ', invalid_items))))
        );
    );
);

_isUnstackable(item) -> (
    return(item != 'air' && stack_limit(item) == 1);
);

_handleUnstackableItems(item_tuples) -> (
    unstackable_items = sort(_removeDuplicates(filter(map(item_tuples, _:0), !_isInvalidItem(_) && _isUnstackable(_))));
    if(unstackable_items,
        if(
            global_settings:'invalid_items_mode' == 'error',
                _error(str(global_error_messages:'CANT_USE_UNSTACKABLES', join(', ', unstackable_items))),
            global_settings:'invalid_items_mode' == 'skip',
                print(format('f » ', 'rb WARNING!', str('g  Skipped %d unstackable items: %s', length(unstackable_items), join(', ', unstackable_items)))),
            // else
                print(format('f » ', str('g Ignored %d unstackable items: %s', length(unstackable_items), join(', ', unstackable_items))))
        );
    );
);

_getReadingComparators(pos) -> (
    comparators = [];
    map(['north', 'east', 'south', 'west'],
        block1 = block(pos_offset(pos, _, -1));
        if(
            block1 == 'comparator' && block_state(block1, 'facing') == _,
                comparators += block1,
            solid(block1) && inventory_has_items(block1) == null,
                block2 = block(pos_offset(block1, _, -1));
                if(block2 == 'comparator' && block_state(block2, 'facing') == _, comparators += block2);
        );
    );
    return(comparators);
);

_updateComparators(block) -> (
    for(_getReadingComparators(block), update(_));
);

_getFacing(player) -> (
    facing = query(player, 'facing');
    return(
        if(
            facing == 'down' || facing == 'up',
                query(player, 'facing', 1),
            // else
                facing
        )
    );
);

_checkPredicate(block, block_predicate) -> (
    [predicate_block, predicate_block_tag, predicate_block_states, predicate_block_data] = block_predicate;
    block_states = block_state(block);
    block_data = block_data(block);
    return(
        (!predicate_block || block == predicate_block) &&
        (!predicate_block_tag || block_tags(block, predicate_block_tag)) &&
        all(predicate_block_states, block_states:_ == predicate_block_states:_) &&
        (!predicate_block_data || all(parse_nbt(predicate_block_data), block_data:_ == predicate_block_data:_))
    );
);

_getItemFromBlock(block) -> (
    if(item_list()~block != null, return(str(block)));

    return(
        if(
            block == 'bamboo_sapling', 'bamboo',
            block == 'beetroots', 'beetroot',
            block == 'big_dripleaf_stem', 'big_dripleaf',
            block == 'carrots', 'carrot',
            block == 'cocoa', 'cocoa_beans',
            block == 'pitcher_crop', 'pitcher_plant',
            block == 'potatoes', 'potato',
            block == 'powder_snow', 'powder_snow_bucket',
            block == 'redstone_wire', 'redstone',
            block == 'sweet_berry_bush', 'sweet_berries',
            block == 'tall_seagrass', 'seagrass',
            block == 'torchflower_crop', 'torchflower',
            block == 'tripwire', 'string',
            block~'cake', 'cake',
            block~'cauldron', 'cauldron',
            block~'cave_vine', 'glow_berries',
            block~'melon', 'melon_seeds',
            block~'pumpkin', 'pumpkin_seeds',
            block~'azalea_bush', replace(replace(block, '_bush', ''), 'potted_', ''),
            block~'plant', replace(block, '_plant', ''),
            block~'potted', replace(block, 'potted_', ''),
            block~'wall', replace(block, 'wall_', '')
        )
    );
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

_itemScreen(items, name) -> (
    pages = map(range(length(items) / 45), slice(items, _i * 45, min(length(items), (_i + 1) * 45)));

    _setMenuInfo(screen, page_count, pages_length, items_length) -> (
        name = _formatTextComponent({'text' -> str('Page %d/%d', page_count % pages_length + 1, pages_length), 'color' -> global_color, 'italic' -> false});
        lore = [_formatTextComponent({'text' -> str('%s entries', items_length), 'color' -> 'gray', 'italic' -> false})];
        inventory_set(screen, 49, 1, 'paper', encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> {'custom_name' -> name, 'lore' -> lore}, 'id' -> 'paper'}, {'display' -> {'Name' -> name, 'Lore' -> lore}}), true));
    );

    _setMenuItems(screen, page) -> (
        loop(45, [item, nbt] = page:_; inventory_set(screen, _, if(_ < length(page), 1, 0), item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true)));
    );

    global_current_page:player() = 0;

    screen = create_screen(player(), 'generic_9x6', name, _(screen, player, action, data, outer(pages), outer(items)) -> (
        if(length(pages) > 1 && action == 'pickup' && (data:'slot' == 48 || data:'slot' == 50),
            page = if(data:'slot' == 48, pages:(global_current_page:player += -1), data:'slot' == 50, pages:(global_current_page:player += 1));
            _setMenuInfo(screen, global_current_page:player, length(pages), length(items));
            _setMenuItems(screen, page);
        );
        if(action == 'pickup_all' || action == 'quick_move' || (action != 'clone' && data:'slot' != null && 0 <= data:'slot' <= 44) || (45 <= data:'slot' <= 53), return('cancel'));
    ));

    _setMenuItems(screen, pages:0);

    for(range(45, 54), inventory_set(screen, _, 1, 'gray_stained_glass_pane', if(system_info('game_pack_version') >= 33, {'components' -> if(system_info('game_pack_version') >= 64, {'tooltip_display' -> {'hide_tooltip' -> true}}, {'hide_tooltip' -> {}}), 'id' -> 'gray_stained_glass_pane'}, {'display' -> {'Name' -> _formatTextComponent('')}})));
    _setMenuInfo(screen, global_current_page:player(), length(pages), length(items));
    if(length(pages) > 1,
        previous_page_name = _formatTextComponent({'text' -> 'Previous Page', 'color' -> global_color, 'italic' -> false});
        next_page_name = _formatTextComponent({'text' -> 'Next Page', 'color' -> global_color, 'italic' -> false});
        inventory_set(screen, 48, 1, 'arrow', encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> {'custom_name' -> previous_page_name}, 'id' -> 'arrow'}, {'display' -> {'Name' -> previous_page_name}}), true));
        inventory_set(screen, 50, 1, 'arrow', encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> {'custom_name' -> previous_page_name}, 'id' -> 'arrow'}, {'display' -> {'Name' -> previous_page_name}}), true));
    );
);

// CONFIG

config = read_file('config', 'json');
global_default_stackable_dummy_item = ['structure_void', if(system_info('game_pack_version') >= 33, {'custom_name' -> _formatTextComponent('╚═Dummy═╝')}, {'display' -> {'Name' -> _formatTextComponent('╚═Dummy═╝')}})];
global_default_unstackable_dummy_item = ['shears', null];
stackable_dummy_item = config:'stackable_dummy_item';
global_stackable_dummy_item = if(stackable_dummy_item && type(stackable_dummy_item) == 'string', _parseEntry(stackable_dummy_item), copy(global_default_stackable_dummy_item));
unstackable_dummy_item = config:'unstackable_dummy_item';
global_unstackable_dummy_item = if(unstackable_dummy_item && type(unstackable_dummy_item) == 'string', _parseEntry(unstackable_dummy_item), copy(global_default_unstackable_dummy_item));
global_default_settings = {
        'shulker_box_color' -> 'default',
        'item_frame_type' -> 'item_frame', // item_frame | glow_item_frame
        'filter_item_amount' -> 2,
        'double_chest_direction' -> 'right', // left | right
        'extra_containers_mode' -> 'no_fill', // repeat | no_fill | dummy_item | air
        'invalid_items_mode' -> 'error', // error | skip | no_fill | dummy_item | air
        'air_mode' -> 'default', // default | dummy_item | invalid
        'placing_offset' -> 1,
        'skip_empty_spots' -> false,
        'replace_blocks' -> false,
        'minimal_filling' -> false,
        'prefer_unstackables' -> false,
        'encoder_chest_max_filling' -> true,
        'encoder_chest_fill_all_slots' -> true,
        'encoder_chest_item_amount' -> 2
};
global_settings = {};
for(global_default_settings, global_settings:_ = config:_ || global_default_settings:_);

// MAIN

menu() -> (
    texts = [
       'fs ' + ' ' * 80, ' \n',
        str('%sb StorageTechX', global_color), if(_checkVersion('1.4.57'), ...['@https://github.com/CommandLeo/scarpet/wiki/StorageTechX', '^g Click to visit the wiki']), 'g  by ', str('%sb CommandLeo', global_color), '^g https://github.com/CommandLeo', if(_checkVersion('1.4.57'), '@https://github.com/CommandLeo'), ' \n\n',
        'g A suite of tools for Storage Tech.', '  \n',
        'g Run ', str('%s /%s help', global_color, system_info('app_name')), str('!/%s help', system_info('app_name')), '^g Click to run the command', 'g  to see a list of all the commands.', '  \n',
        'fs ' + ' ' * 80
    ];
    print(format(texts));
);

help(page) -> (
    if(page < 1 || page > length(global_help_pages), _error(global_error_message:'INVALID_PAGE_NUMBER'));
    page = page - 1;
    previous_page = (page - 1) % length(global_help_pages) + 1;
    next_page = (page + 1) % length(global_help_pages) + 1;
    texts = ['fs ' + ' ' * 80, ' \n', ...global_help_pages:page, 'fs ' + ' ' * 31, '  ', 'fb «', '^g Previous page', '!/%app_name% help ' + previous_page, str('g \ Page %d/%d ', page + 1, length(global_help_pages)), 'fb »', '^g Next page', '!/%app_name% help ' + next_page, '  ', 'fs ' + ' ' * 30];
    replacement_map = {'%app_name%' -> system_info('app_name'), '%color%' -> global_color};
    print(format(map(texts, reduce(pairs(replacement_map), replace(_a, ..._), _))));
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
    if(!module_data, _error(global_error_messages:'MODULE_NOT_FOUND'));

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
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = _readItemLayout(item_layout);
    if(!items, _error(str(global_error_messages:'ITEM_LAYOUT_EMPTY', item_layout)));

    createItemList(name, items);
);

createItemListFromAllItems(name, obtainability, stackability) -> (
    if(global_obtainabilities~obtainability == null, _error(global_error_messages:'INVALID_OBTAINABILITY'));
    if(global_stackabilities~stackability == null, _error(global_error_messages:'INVALID_STACKABILITY'));

    items = map(item_list(), [_, null]);
    survival_unobtainable_items = system_variable_get('survival_unobtainable_items');
    junk_items = system_variable_get('junk_items');
    firework_rockets = map(range(3), ['firework_rocket', if(system_info('game_pack_version') >= 33, {'fireworks' -> {'flight_duration' -> _ + 1}}, {'Fireworks' -> {'Flight' -> _ + 1}})]);
    if((obtainability == 'main_storage' || obtainability == 'survival_obtainables') && (!survival_unobtainable_items || !junk_items), _error(global_error_messages:'ALLITEMS_NOT_INSTALLED'));
    if(obtainability == 'main_storage', put(items, items~['firework_rocket', null], firework_rockets, 'extend'));
    items = filter(items,
        [item, nbt] = _;
        obtainability_check = if(
            obtainability == 'main_storage',
                survival_unobtainable_items~item == null && (junk_items~item == null || nbt != null) && item~'shulker_box' == null,
            obtainability == 'survival_obtainables',
                survival_unobtainable_items~item == null,
            true
        );
        stackability_check = !stackability || global_stackabilities:stackability~stack_limit(item) != null;
        item != 'air' && obtainability_check && stackability_check;
    );

    createItemList(name, items);
);

createItemListFromContainers(name, from_pos, to_pos) -> (
    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error(global_error_messages:'NOT_LOOKING_AT_ANY_BLOCK'));
        from_pos = trace;
    );
    if(!to_pos,
        if(inventory_has_items(from_pos) == null, _error(global_error_messages:'NOT_A_CONTAINER'));
        to_pos = from_pos;
    );

    items = [];
    container_found = false;

    volume(from_pos, to_pos,
        block = _;

        if(inventory_has_items(from_pos),
            container_found = true;
            loop(inventory_size(block), item_tuple = inventory_get(block, _); if(item_tuple, [item, count, nbt] = item_tuple; items += [item, if(system_info('game_pack_version') >= 33, nbt:'components', nbt)]));
        );
    );

    if(!container_found, _error(global_error_messages:'NO_CONTAINER_FOUND'));
    if(!items, _error(global_error_messages:'NO_ITEMS_FOUND'));

    createItemList(name, _removeDuplicates(items));
);

createItemListFromArea(name, from_pos, to_pos) -> (
    items = {};
    ignored_blocks = {};
    volume(from_pos, to_pos, item = _getItemFromBlock(_); if(item, items += [item, null], ignored_blocks += str(_)));

    if(ignored_blocks, print(format('f » ', str('g The following blocks were ignored: %s', join(', ', keys(ignored_blocks))))));
    if(!items, _error(global_error_messages:'SELECTED_AREA_EMPTY'));

    createItemList(name, keys(items));
);

createItemList(name, items) -> (
    item_list_path = str('item_lists/%s', name);
    if(list_files('item_lists', 'shared_text')~item_list_path != null, _error(global_error_messages:'ITEM_LIST_ALREADY_EXISTS'));
    if(!items, _error(global_error_messages:'NO_ITEMS_PROVIDED'));

    invalid_items = filter(map(items, _:0), item_list()~_ == null);
    if(invalid_items, _error(str(global_error_messages:'INVALID_ITEMS', join(', ', sort(_removeDuplicates(invalid_items))))));

    success = write_file(item_list_path, 'shared_text', map(items, _itemToString(_)));

    if(!success, _error(global_error_messages:'FILE_WRITING_ERROR'));
    print(format('f » ', 'g Successfully created the ', str('%s %s', global_color, name), str('^g /%s item_list %s %s', system_info('app_name'), if(_checkVersion('1.4.57'), 'view', 'info'), name), str('!/%s item_list %s %s', system_info('app_name'), if(_checkVersion('1.4.57'), 'view', 'info'), name), 'g  item list'));
    run('playsound block.note_block.pling master @s');
);

renameItemList(item_list, name) -> (
    item_list_path = str('item_lists/%s', item_list);
    new_item_list_path = str('item_lists/%s', name);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));
    if(list_files('item_lists', 'shared_text')~new_item_list_path != null, _error(global_error_messages:'ITEM_LIST_ALREADY_EXISTS'));

    success1 = write_file(new_item_list_path, 'shared_text', read_file(item_list_path, 'shared_text'));
    if(!success1, _error(global_error_messages:'FILE_WRITING_ERROR'));
    success2 = delete_file(item_list_path, 'shared_text');
    if(!success2, _error(global_error_messages:'FILE_DELETION_ERROR'));

    print(format('f » ', 'g Successfully renamed the ', str('%s %s', global_color, item_list), 'g  item list to ', str('%s %s', global_color, name)));
    run('playsound block.note_block.pling master @s');
);

cloneItemList(item_list, name) -> (
    item_list_path = str('item_lists/%s', item_list);
    new_item_list_path = str('item_lists/%s', name);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));
    if(list_files('item_lists', 'shared_text')~new_item_list_path != null, _error(global_error_messages:'ITEM_LIST_ALREADY_EXISTS'));

    success = write_file(new_item_list_path, 'shared_text', read_file(item_list_path, 'shared_text'));
    if(!success, _error(global_error_messages:'FILE_WRITING_ERROR'));

    print(format('f » ', 'g Successfully cloned the ', str('%s %s', global_color, item_list), 'g  item list into the ', str('%s %s', global_color, name), 'g  item list'));
    run('playsound block.note_block.pling master @s');
);

deleteItemList(item_list, confirmation) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    if(!confirmation, exit(print(format('f » ', 'g Click ', str('%sbu here', global_color), str('^g /%s item_list delete %s confirm', system_info('app_name'), item_list), str('!/%s item_list delete %s confirm', system_info('app_name'), item_list), 'g  to confirm the deletion of the ', str('%s %s', global_color, item_list), 'g  item list'))));
    success = delete_file(item_list_path, 'shared_text');
    if(!success, _error(global_error_messages:'FILE_DELETION_ERROR'));

    print(format('f » ', 'g Successfully deleted the ', str('%s %s', global_color, item_list), 'g  item list'));
    run('playsound item.shield.break master @s');
);

editItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));
    items = _readItemList(item_list);
    if(!items, _error(str(global_error_messages:'ITEM_LIST_EMPTY', item_list)));

    print(format(reduce(items, [item, nbt] = _; [..._a, ' \n  ', '#EB4D4Bb ❌', '^r Remove the item', str('?/%s item_list edit %s remove index %d', system_info('app_name'), item_list, _i), '  ', str('g%s %s%s', if(item_list()~item == null, 's', ''), item, if(nbt, '*', '')), str('!/%s', _giveCommand(item, nbt)), if(item_list()~item == null, '^g Invalid item', nbt, str('^g %s', nbt), ''), str('!/%s', _giveCommand(item, nbt))], ['f » ', 'g Edit the ', str('%s %s', global_color, item_list), 'g  item list: ', '#26DE81b (+)', '^#26DE81 Add more items', str('?/%s item_list edit %s add', system_info('app_name'), item_list)])));
);

addEntriesToItemListFromItems(item_list, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    addEntriesToItemList(item_list, items);
);

addEntriesToItemListFromItemList(item_list, other_item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    other_item_list_path = str('item_lists/%s', other_item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));
    if(list_files('item_lists', 'shared_text')~other_item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', other_item_list)));

    addEntriesToItemList(item_list, _readItemList(other_item_list));
);

addEntriesToItemListFromItemLayout(item_list, item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = _readItemLayout(item_layout);
    if(!items, _error(str(global_error_messages:'ITEM_LAYOUT_EMPTY', item_layout)));

    addEntriesToItemList(item_list, items);
);

addEntriesToItemListFromContainers(item_list, from_pos, to_pos) -> (
    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error(global_error_messages:'NOT_LOOKING_AT_ANY_BLOCK'));
        from_pos = trace;
    );
    if(!to_pos,
        if(inventory_has_items(from_pos) == null, _error(global_error_messages:'NOT_A_CONTAINER'));
        to_pos = from_pos;
    );

    items = [];
    container_found = false;

    volume(from_pos, to_pos,
        block = _;

        if(inventory_has_items(from_pos),
            container_found = true;
            loop(inventory_size(block), item_tuple = inventory_get(block, _); if(item_tuple, [item, count, nbt] = item_tuple; items += [item, if(system_info('game_pack_version') >= 33, nbt:'components', nbt)]));
        );
    );

    if(!container_found, _error(global_error_messages:'NO_CONTAINER_FOUND'));
    if(!items, _error(global_error_messages:'NO_ITEMS_FOUND'));

    addEntriesToItemList(item_list, _removeDuplicates(items));
);

addEntriesToItemListFromArea(item_list, from_pos, to_pos) -> (
    items = {};
    ignored_blocks = {};
    volume(from_pos, to_pos, item = _getItemFromBlock(_); if(item, items += [item, null], ignored_blocks += str(_)));

    if(ignored_blocks, print(format('f » ', str('g The following blocks were ignored: %s', join(', ', keys(ignored_blocks))))));
    if(!items, _error(global_error_messages:'SELECTED_AREA_EMPTY'));

    addEntriesToItemList(item_list, keys(items));
);

addEntriesToItemList(item_list, entries) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(global_error_messages:'NO_ITEMS_TO_ADD'));

    invalid_items = filter(map(items, _:0), item_list()~_ == null);
    if(invalid_items, _error(str(global_error_messages:'INVALID_ITEMS', join(', ', sort(_removeDuplicates(invalid_items))))));

    success = write_file(item_list_path, 'shared_text', map(entries, _itemToString(_)));

    if(!success, _error(global_error_messages:'FILE_WRITING_ERROR'));
    print(format('f » ', 'g Successfully added the items to the ', str('%s %s', global_color, item_list), 'g  item list'));
    run('playsound block.note_block.pling master @s');
);

removeEntriesFromItemListFromItems(item_list, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    removeEntriesFromItemList(item_list, items);
);

removeEntriesFromItemListFromItemList(item_list, other_item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    other_item_list_path = str('item_lists/%s', other_item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));
    if(list_files('item_lists', 'shared_text')~other_item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', other_item_list)));

    removeEntriesFromItemList(item_list, _readItemList(other_item_list));
);

removeEntriesFromItemList(item_list, entries) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);

    l1 = length(items);
    items = filter(items, [item, nbt] = _; entries~[item, nbt] == null && entries~[item, null] == null);
    l2 = length(items);
    if(l2 == l1, _error(global_error_messages:'NO_ENTRIES_TO_REMOVE'));

    success1 = delete_file(item_list_path, 'shared_text');
    if(!success1, _error(global_error_messages:'FILE_DELETION_ERROR'));
    success2 = write_file(item_list_path, 'shared_text', map(items, _itemToString(_)));
    if(!success2, _error(global_error_messages:'FILE_WRITING_ERROR'));

    print(format('f » ', str('g Successfully removed %d items from the ', l1 - l2), str('%s %s', global_color, item_list), 'g  item list'));
    run('playsound block.note_block.pling master @s');
);

removeEntriesFromItemListWithIndex(item_list, index) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    if(index < 0 || index >= length(items), _error(global_error_messages:'INVALID_INDEX'));

    delete(items:index);

    delete_file(item_list_path, 'shared_text');
    success = write_file(item_list_path, 'shared_text', map(items, _itemToString(_)));

    if(!success, _error(global_error_messages:'FILE_WRITING_ERROR'));
    print(format('f » ', 'g Successfully removed the item from the ', str('%s %s', global_color, item_list), 'g  item list'));
    run('playsound block.note_block.pling master @s');
);

listItemLists() -> (
    files = list_files('item_lists', 'shared_text');
    if(!files, _error(global_error_messages:'NO_ITEM_LISTS'));

    item_lists = map(files, slice(_, length('item_lists') + 1));

    texts = reduce(item_lists,
        [..._a, if(_i == 0, '', 'g , '), str('%s %s', global_color, _), str('^g /%s item_list %s %s', system_info('app_name'), if(_checkVersion('1.4.57'), 'view', 'info'), _), str('?/%s item_list %s %s', system_info('app_name'), if(_checkVersion('1.4.57'), 'view', 'info'), _)],
        ['f » ', 'g Saved item lists: ']
    );
    print(format(texts));
);

infoItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str(global_error_messages:'ITEM_LIST_EMPTY', item_list)));

    texts = reduce(items, [item, nbt] = _; [..._a, if(length(items) < 12, ' \n', if(_i == 0, '', 'g , ')), str('g %s', if(length(items) < 12, '   ', '')), str('g%s %s%s', if(item_list()~item == null, 's', ''), item, if(nbt, '*', '')), str('!/%s', _giveCommand(item, nbt)), if(item_list()~item == null, '^g Invalid item', nbt, str('^g %s', nbt), '')], ['f » ', 'g The ', str('%s %s', global_color, item_list), 'g  item list contains the following items: ']);
    print(format(texts));
);

viewItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    items = filter(items, [item, nbt] = _; item != 'air' && item_list()~item != null);
    if(!items, _error(str(global_error_messages:'ITEM_LIST_EMPTY', item_list)));

    _itemScreen(items, item_list)
);

// SETTINGS

settings() -> (
    texts = [
        'fs ' + ' ' * 80, ' \n',
        str('%sb StorageTechX Settings', global_color), ' \n\n',
        'g Shulker Box Color', '^g The color of shulker boxes', str('?/%s settings shulker_box_color', system_info('app_name')), 'f  - ', str('%sb %s', global_shulker_box_colors:(global_settings:'shulker_box_color'), upper(global_settings:'shulker_box_color')), ' \n',
        'g Item Frame Type', '^g The type of item frame to use (normal or glowing)', str('?/%s settings item_frame_type', system_info('app_name')), 'f  - ', str('db %s', upper(global_item_frame_types:(global_settings:'item_frame_type'))), ' \n',
        'g Filter Item Amount', '^g The amount of filter items in the first slot of low threshold item filters', str('?/%s settings filter_item_amount', system_info('app_name')), 'f  - ', str('%sb %s', global_color, global_settings:'filter_item_amount'), ' \n',
        'g Double Chest Direction', '^g The direction where to place the second half of double chests', str('?/%s settings double_chest_direction', system_info('app_name')), 'f  - ', str('wb %s', upper(global_settings:'double_chest_direction')), ' \n',
        'g Extra Containers Mode', '^g How extra containers should be filled', str('?/%s settings extra_containers_mode', system_info('app_name')), 'f  - ', str('%sb %s', global_extra_containers_mode_options:(global_settings:'extra_containers_mode'), upper(global_settings:'extra_containers_mode')), ' \n',
        'g Invalid Items Mode', '^g How invalid items should be handled', str('?/%s settings invalid_items_mode', system_info('app_name')), 'f  - ', str('%sb %s', global_invalid_items_mode_options:(global_settings:'invalid_items_mode'), upper(global_settings:'invalid_items_mode')), ' \n',
        'g Air Mode', '^g How air items should be handled', str('?/%s settings air_mode', system_info('app_name')), 'f  - ', str('%sb %s', global_air_mode_options:(global_settings:'air_mode'), upper(global_settings:'air_mode')), ' \n',
        'g Placing Offset', '^g The offset between placed blocks', str('?/%s settings placing_offset', system_info('app_name')), 'f  - ', str('%sb %s', global_color, global_settings:'placing_offset'), ' \n',
        'g Skip Empty Spots', '^g Whether to skip empty spots', str('?/%s settings skip_empty_spots', system_info('app_name')), 'f  - ', if(global_settings:'skip_empty_spots', 'lb TRUE', 'rb FALSE'), ' \n',
        'g Replace Blocks', '^g Whether to replace non-air blocks when placing blocks', str('?/%s settings replace_blocks', system_info('app_name')), 'f  - ', if(global_settings:'replace_blocks', 'lb TRUE', 'rb FALSE'), ' \n',
        'g Minimal Filling', '^g Whether to fill the first slot of standard item filters with only a single filter item regardless', str('?/%s settings minimal_filling', system_info('app_name')), 'f  - ', if(global_settings:'minimal_filling', 'lb TRUE', 'rb FALSE'), ' \n',
        'g Prefer Unstackables', '^g Whether to use unstackable dummy items instead of full stacks of stackable dummy items', str('?/%s settings prefer_unstackables', system_info('app_name')), 'f  - ', if(global_settings:'prefer_unstackables', 'lb TRUE', 'rb FALSE'), ' \n',
        'g Encoder Chest Max Filling', '^g Whether to use normal items instead of dummy items to adjust the signal strength of an encoder chest, if needed to fit more item types', str('?/%s settings encoder_chest_max_filling', system_info('app_name')), 'f  - ', if(global_settings:'encoder_chest_max_filling', 'lb TRUE', 'rb FALSE'), ' \n',
        'g Encoder Chest Fill All Slots', '^g Whether to fill all empty slots in an encoder chest with dummy items', str('?/%s settings encoder_chest_fill_all_slots', system_info('app_name')), 'f  - ', if(global_settings:'encoder_chest_fill_all_slots', 'lb TRUE', 'rb FALSE'), ' \n',
        'g Encoder Chest Item Amount', '^g The amount of items in each slot of an encoder chest', str('?/%s settings encoder_chest_item_amount', system_info('app_name')), 'f  - ', str('%sb %s', global_color, global_settings:'encoder_chest_item_amount'), ' \n',
        'fs ' + ' ' * 80
    ];
    print(format(texts));
);

printShulkerBoxColorSetting() -> (
    print(format('f » ', 'g The shulker box color is ', str('%sb %s', global_shulker_box_colors:(global_settings:'shulker_box_color'), upper(global_settings:'shulker_box_color'))));
);

setShulkerBoxColorSetting(color) -> (
    if(
        color == null,
            global_settings:'shulker_box_color' = global_default_settings:'shulker_box_color';
            print(format('f » ', 'g The shulker box color has been reset to its default value')),

        // else
            if(global_shulker_box_colors~color == null, _error(global_error_messages:'INVALID_SHULKER_BOX_COLOR'));

            global_settings:'shulker_box_color' = color;
            print(format('f » ', 'g The shulker box color has been set to ', str('%sb %s', global_shulker_box_colors:color, upper(color))));
    );
);

printItemFrameTypeSetting() -> (
    print(format('f » ', 'g The item frame type is ', str('db %s', upper(global_item_frame_types:(global_settings:'item_frame_type')))));
);

setItemFrameTypeSetting(item_frame_type) -> (
    if(
        item_frame_type == null,
            global_settings:'item_frame_type' = global_default_settings:'item_frame_type';
            print(format('f » ', 'g The item frame type has been reset to its default value')),

        // else
            if(global_item_frame_types~item_frame_type == null, _error(global_error_messages:'INVALID_ITEM_FRAME_TYPE'));

            global_settings:'item_frame_type' = item_frame_type;
            print(format('f » ', 'g The item frame type has been set to ', str('db %s', upper(global_item_frame_types:item_frame_type))));
    );
);

printFilterItemAmountSetting() -> (
    print(format('f » ', 'g The amount of filter items for low threshold filters is ', str('%s %d', global_color, global_settings:'filter_item_amount')));
);

setFilterItemAmountSetting(amount) -> (
    if(
        amount == null,
            global_settings:'filter_item_amount' = global_default_settings:'filter_item_amount';
            print(format('f » ', 'g The amount of filter items for low threshold filters has been reset to its default value')),

        // else
            if(type(amount) != 'number' || amount < 1 || amount > 63, _error(global_error_messages:'INVALID_ITEM_AMOUNT'));

            global_settings:'filter_item_amount' = amount;
            print(format('f » ', 'g The amount of filter items for low threshold filters has been set to ', str('%s %d', global_color, amount)));
    );
);

printDoubleChestDirectionSetting() -> (
    print(format('f » ', 'g The relative direction where to place the second half of double chests is ', str('wb %s', upper(global_settings:'double_chest_direction'))));
);

setDoubleChestDirectionSetting(direction) -> (
    if(
        direction == null,
            global_settings:'double_chest_direction' = global_default_settings:'double_chest_direction';
            print(format('f » ', 'g The relative direction where to place the second half of double chests has been reset to its default value')),

        // else
            if(global_double_chest_direction_options~direction == null, _error(global_error_messages:'INVALID_DOUBLE_CHEST_DIRECTION'));

            global_settings:'double_chest_direction' = direction;
            print(format('f » ', 'g The relative direction where to place the second half of double chests has been set to ', str('wb %s', upper(direction))));
    );
);

printExtraContainersModeSetting() -> (
    print(format('f » ', 'g The mode to fill extra containers is ', str('%sb %s', global_extra_containers_mode_options:(global_settings:'extra_containers_mode'), upper(global_settings:'extra_containers_mode'))));
);

setExtraContainersModeSetting(extra_containers_mode) -> (
    if(
        extra_containers_mode == null,
            global_settings:'extra_containers_mode' = global_default_settings:'extra_containers_mode';
            print(format('f » ', 'g The mode to fill extra containers has been reset to its default value')),

        // else
            if(global_extra_containers_mode_options~extra_container_mode == null, _error(global_error_messages:'INVALID_EXTRA_CONTAINERS_MODE'));

            global_settings:'extra_containers_mode' = extra_containers_mode;
            print(format('f » ', 'g The mode to fill extra containers has been set to ', str('%sb %s', global_extra_containers_mode_options:extra_containers_mode, upper(extra_containers_mode))));
    );
);

printInvalidItemsModeSetting() -> (
    print(format('f » ', 'g The mode to handle invalid items is ', str('%sb %s', global_invalid_items_mode_options:(global_settings:'invalid_items_mode'), upper(global_settings:'invalid_items_mode'))));
);

setInvalidItemsModeSetting(invalid_items_mode) -> (
    if(
        invalid_items_mode == null,
            global_settings:'invalid_items_mode' = global_default_settings:'invalid_items_mode';
            print(format('f » ', 'g The mode to handle invalid items has been reset to its default value')),

        // else
            if(global_invalid_items_mode_options~invalid_items_mode == null, _error(global_error_messages:'INVALID_INVALID_ITEMS_MODE'));

            global_settings:'invalid_items_mode' = invalid_items_mode;
            print(format('f » ', 'g The mode to handle invalid items has been set to ', str('%sb %s', global_invalid_items_mode_options:invalid_items_mode, upper(invalid_items_mode))));
    );
);

printAirModeSetting() -> (
    print(format('f » ', 'g The mode to handle air items is ', str('%sb %s', global_air_mode_options:(global_settings:'air_mode'), upper(global_settings:'air_mode'))));
);

setAirModeSetting(air_mode) -> (
    if(
        air_mode == null,
            global_settings:'air_mode' = global_default_settings:'air_mode';
            print(format('f » ', 'g The mode to handle air items has been reset to its default value')),

        // else
            if(global_air_mode_options~air_mode == null, _error(global_error_messages:'INVALID_AIR_MODE'));

            global_settings:'air_mode' = air_mode;
            print(format('f » ', 'g The mode to handle air items has been set to ', str('%sb %s', global_air_mode_options:air_mode, upper(air_mode))));
    );
);

printPlacingOffsetSetting() -> (
    print(format('f » ', 'g The placing offset is ', str('%s %d', global_color, global_settings:'placing_offset')));
);

setPlacingOffsetSetting(offset) -> (
    if(
        offset == null,
            global_settings:'placing_offset' = global_default_settings:'placing_offset';
            print(format('f » ', 'g The placing offset has been reset to its default value')),

        // else
            if(type(offset) != 'number' || offset < 1 || offset > 100, _error(global_error_messages:'INVALID_PLACING_OFFSET'));

            global_settings:'placing_offset' = offset;
            print(format('f » ', 'g The placing offset has been set to ', str('%s %d', global_color, offset)));
    );
);

printReplaceBlocksSetting() -> (
    print(format('f » ', 'g Replacing blocks is ', if(global_settings:'replace_blocks', 'l enabled', 'r disabled')));
);

setReplaceBlocksSetting(value) -> (
    if(
        value == null,
            global_settings:'replace_blocks' = global_default_settings:'replace_blocks';
            print(format('f » ', 'g Replacing blocks has been ', if(global_settings:'replace_blocks', 'l enabled', 'r disabled'))),

        // else
            global_settings:'replace_blocks' = bool(value);
            print(format('f » ', 'g Replacing blocks has been ', if(global_settings:'replace_blocks', 'l enabled', 'r disabled')));
    );
);

printSkipEmptySpotsSetting() -> (
    print(format('f » ', 'g Skipping empty spots is ', if(global_settings:'skip_empty_spots', 'l enabled', 'r disabled')));
);

setSkipEmptySpotsSetting(value) -> (
    if(
        value == null,
            global_settings:'skip_empty_spots' = global_default_settings:'skip_empty_spots';
            print(format('f » ', 'g Skipping empty spots has been ', if(global_settings:'skip_empty_spots', 'l enabled', 'r disabled'))),

        // else
            global_settings:'skip_empty_spots' = bool(value);
            print(format('f » ', 'g Skipping empty spots has been ', if(global_settings:'skip_empty_spots', 'l enabled', 'r disabled')));
    )
);

printMinimalFillingSetting() -> (
    print(format('f » ', 'g Minimal filling is ', if(global_settings:'minimal_filling', 'l enabled', 'r disabled')));
);

setMinimalFillingSetting(value) -> (
    if(
        value == null,
            global_settings:'minimal_filling' = global_default_settings:'minimal_filling';
            print(format('f » ', 'g Minimal filling has been ', if(global_settings:'minimal_filling', 'l enabled', 'r disabled'))),

        // else
            global_settings:'minimal_filling' = bool(value);
            print(format('f » ', 'g Minimal filling has been ', if(global_settings:'minimal_filling', 'l enabled', 'r disabled')));
    );
);

printPreferUnstackables() -> (
    print(format('f » ', 'g Preferring unstackables is ', if(global_settings:'prefer_unstackables', 'l enabled', 'r disabled')));
);

setPreferUnstackables(value) -> (
    if(
        value == null,
            global_settings:'prefer_unstackables' = global_default_settings:'prefer_unstackables';
            print(format('f » ', 'g Preferring unstackables has been ', if(global_settings:'prefer_unstackables', 'l enabled', 'r disabled'))),

        // else
            global_settings:'prefer_unstackables' = bool(value);
            print(format('f » ', 'g Preferring unstackables has been ', if(global_settings:'prefer_unstackables', 'l enabled', 'r disabled')));
    );
);

printEncoderChestMaxFilling() -> (
    print(format('f » ', 'g Maximum filling of encoder chests is ', if(global_settings:'encoder_chest_max_filling', 'l enabled', 'r disabled')));
);

setEncoderChestMaxFilling(value) -> (
    if(
        value == null,
            global_settings:'encoder_chest_max_filling' = global_default_settings:'encoder_chest_max_filling';
            print(format('f » ', 'g Maximum filling of encoder chests has been ', if(global_settings:'encoder_chest_max_filling', 'l enabled', 'r disabled'))),

        // else
            global_settings:'encoder_chest_max_filling' = bool(value);
            print(format('f » ', 'g Maximum filling of encoder chests has been ', if(global_settings:'encoder_chest_max_filling', 'l enabled', 'r disabled')));
    );
);

printEncoderChestFillAllSlots() -> (
    print(format('f » ', 'g Filling all slots of encoder chests is ', if(global_settings:'encoder_chest_max_filling', 'l enabled', 'r disabled')));
);

setEncoderChestFillAllSlots(value) -> (
    if(
        value == null,
            global_settings:'encoder_chest_fill_all_slots' = global_default_settings:'encoder_chest_fill_all_slots';
            print(format('f » ', 'g Filling all slots of encoder chests has been ', if(global_settings:'encoder_chest_fill_all_slots', 'l enabled', 'r disabled'))),

        // else
            global_settings:'encoder_chest_fill_all_slots' = bool(value);
            print(format('f » ', 'g Filling all slots of encoder chests has been ', if(global_settings:'encoder_chest_fill_all_slots', 'l enabled', 'r disabled')));
    );
);

printEncoderChestItemAmount() -> (
    print(format('f » ', 'g The amount of items in each slot of an encoder chest is ', str('%s %d', global_color, global_settings:'encoder_chest_item_amount')));
);

setEncoderChestItemAmount(amount) -> (
    if(
        amount == null,
            global_settings:'encoder_chest_item_amount' = global_default_settings:'encoder_chest_item_amount';
            print(format('f » ', 'g The amount of items in each slot of an encoder chest has been reset to its default value')),

        // else
            if(type(amount) != 'number' || amount < 1 || amount > 64, _error(global_error_messages:'INVALID_ITEM_AMOUNT'));

            global_settings:'encoder_chest_item_amount' = amount;
            print(format('f » ', 'g The amount of items in each slot of an encoder chest has been set to ', str('%s %d', global_color, amount)));
    );
);

// DUMMY ITEM

printStackableDummyItem() -> (
    [item, nbt] = global_stackable_dummy_item;
    print(format('f » ', 'g The current stackable dummy item is ', if(nbt, ...[str('%s %s*', global_color, item), str('^g %s', nbt)], str('%s %s', global_color, item)))),
);

setStackableDummyItemFromHand() -> (
    holds = player()~'holds';
    if(!holds, _error(global_error_messages:'NOT_HOLDING_ANY_ITEM'));

    setStackableDummyItem(holds);
);

setStackableDummyItem(item_tuple) -> (
    if(
        !item_tuple,
            global_stackable_dummy_item = global_default_stackable_dummy_item;
            print(format('f » ', 'g The stackable dummy item has been reset')),
        // else
            [item, count, nbt] = item_tuple;
            if(_isUnstackable(item) || item == 'air', _error(global_error_messages:'ONLY_STACKABLES_ALLOWED'));
            if(system_info('game_pack_version') >= 33, nbt = nbt:'components');
            global_stackable_dummy_item = [item, nbt];
            print(format('f » ', 'g The stackable dummy item has been set to ', str('%s %s', global_color, item), if(nbt, str('^g %s', nbt))))
    );
);

giveStackableDummyItem() -> (
    [item, nbt] = global_stackable_dummy_item;
    run(_giveCommand(item, nbt));
);

printUnstackableDummyItem() -> (
    [item, nbt] = global_unstackable_dummy_item;
    print(format('f » ', 'g The current unstackable dummy item is ', if(nbt, ...[str('%s %s*', global_color, item), str('^g %s', nbt)], str('%s %s', global_color, item)))),
);

setUnstackableDummyItemFromHand() -> (
    holds = player()~'holds';
    if(!holds, _error(global_error_messages:'NOT_HOLDING_ANY_ITEM'));

    setUnstackableDummyItem(holds);
);

setUnstackableDummyItem(item_tuple) -> (
    if(
        !item_tuple,
            global_unstackable_dummy_item = global_default_unstackable_dummy_item;
            print(format('f » ', 'g The unstackable dummy item has been reset')),
        // else
            [item, count, nbt] = item_tuple;
            if(!_isUnstackable(item), _error(global_error_messages:'ONLY_UNSTACKABLES_ALLOWED'));
            if(system_info('game_pack_version') >= 33, nbt = nbt:'components');
            global_unstackable_dummy_item = [item, nbt];
            print(format('f » ', 'g The unstackable dummy item has been set to ', str('%s %s', global_color, item), if(nbt, str('^g %s', nbt))))
    );
);

giveUnstackableDummyItem() -> (
    [item, nbt] = global_unstackable_dummy_item;
    run(_giveCommand(item, nbt));
);

// ITEM FILTER

_isValidItemFilter(item_filter) -> (
    return(global_default_item_filters~item_filter != null || 2 <= number(item_filter~'^ss(\\d+)$') <= 15 || 2 <= number(item_filter~'^ssi_ss(\\d+)$') <= 12);
);

_formatItemFilter(item_filter) -> (
    if(!_isValidItemFilter(item_filter), return(item_filter));

    ssi_ss = number(item_filter~'^ssi_ss(\\d+)$');
    ss = number(item_filter~'^ss(\\d+)$');

    return(
        if(
            ss, str('SS%d', ss),
            ssi_ss, str('SSI SS%d', ssi_ss),
            title(replace(item_filter, '_', ' '))
        )
    );
);

_itemFilter(hopper, item_filter, item_tuple) -> (
    if(!_isValidItemFilter(item_filter), return(false));

    ssi_ss = number(item_filter~'^ssi_ss(\\d+)$');
    ss = number(item_filter~'^ss(\\d+)$');
    if(
        ssi_ss, _ssiItemFilter(hopper, item_tuple, ssi_ss, global_settings:'filter_item_amount', global_settings:'minimal_filling'),
        ss, _ssItemFilter(hopper, item_tuple, ss, global_settings:'minimal_filling'),
        item_filter == 'overstacking', _overstackingItemFilter(hopper, item_tuple, global_settings:'filter_item_amount', global_settings:'minimal_filling'),
        item_filter == 'box_sorter', _boxSorterFilter(hopper, item_tuple),
        item_filter == 'stack_separation', _stackSeparationFilter(hopper, item_tuple),
        return(false)
    );
    return(true);
);

_ssiItemFilter(hopper, item_tuple, signal_strength, amount, minimal) -> (
    [item, nbt] = item_tuple;

    total_dummy_amount = floor((ceil(5 * 64 / 14 * (signal_strength - 1)) - (amount + 1) * (64 / stack_limit(item))) / (64 / stack_limit(global_stackable_dummy_item:0)));

    inventory_set(hopper, 0, if(minimal, 1, amount), item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true));
    for(range(1, 5),
        [dummy_item, dummy_nbt] = global_stackable_dummy_item;
        amount = min(total_dummy_amount - 4 + _, stack_limit(dummy_item));
        total_dummy_amount += -amount;

        if(amount == stack_limit(dummy_item) && global_settings:'prefer_unstackables',
            [dummy_item, dummy_nbt] = global_unstackable_dummy_item;
            amount = 1;
        );
        inventory_set(hopper, _, amount, dummy_item, dummy_nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> dummy_nbt, 'id' -> dummy_item}, dummy_nbt), true));
    );

    return(hopper);
);

_ssItemFilter(hopper, item_tuple, signal_strength, minimal) -> (
    [item, nbt] = item_tuple;

    total_amount = floor(ceil(5 * 64 / 14 * (signal_strength - 1)) - 1);
    amount = min(stack_limit(item) - 1, floor((total_amount - 4 * (64 / stack_limit(global_stackable_dummy_item:0))) / (64 / stack_limit(item))));
    total_dummy_amount = (total_amount - amount * (64 / stack_limit(item))) / (64 / stack_limit(global_stackable_dummy_item:0));

    inventory_set(hopper, 0, if(minimal, 1, amount), item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true));
    for(range(1, 5),
        [dummy_item, dummy_nbt] = global_stackable_dummy_item;
        amount = min(total_dummy_amount - 4 + _, stack_limit(dummy_item));
        total_dummy_amount += -amount;

        if(amount == stack_limit(dummy_item) && global_settings:'prefer_unstackables',
            [dummy_item, dummy_nbt] = global_unstackable_dummy_item;
            amount = 1;
        );
        inventory_set(hopper, _, amount, dummy_item, dummy_nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> dummy_nbt, 'id' -> dummy_item}, dummy_nbt), true));
    );

    return(hopper);
);

_overstackingItemFilter(hopper, item_tuple, amount, minimal) -> (
    [item, nbt] = item_tuple;
    [dummy_item, dummy_nbt] = global_stackable_dummy_item;
    [unstackable_dummy_item, unstackable_dummy_nbt] = global_unstackable_dummy_item;
    dummy_amount = floor((64 - (amount + 1) * (64 / stack_limit(item))) / (64 / stack_limit(dummy_item)));

    inventory_set(hopper, 0, if(minimal, 1, amount), item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true));
    inventory_set(hopper, 1, dummy_amount, dummy_item, dummy_nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> dummy_nbt, 'id' -> dummy_item}, dummy_nbt), true));
    inventory_set(hopper, 2, 2, if(system_info('game_pack_version') >= 33, 'shulker_box', ...['enchanted_book', {'StoredEnchantments' -> [{'id' -> 'vanishing_curse', 'lvl' -> 1}]}]));
    for(range(3, 5), inventory_set(hopper, _, 1, unstackable_dummy_item, unstackable_dummy_nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> unstackable_dummy_nbt, 'id' -> unstackable_dummy_item}, unstackable_dummy_nbt), true)));

    return(hopper);
);

_boxSorterFilter(hopper, item_tuple) -> (
    [item, nbt] = item_tuple;

    inventory_set(hopper, 0, 1, item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true));
    for(range(1, 5), inventory_set(hopper, _, 1, 'shulker_box'));

    return(hopper);
);

_stackSeparationFilter(hopper, item_tuple) -> (
    [item, nbt] = item_tuple;
    [dummy_item, dummy_nbt] = if(global_settings:'prefer_unstackables', global_unstackable_dummy_item, global_stackable_dummy_item);

    inventory_set(hopper, 0, stack_limit(item) - 1, item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true));
    for(range(1, 5), inventory_set(hopper, _, stack_limit(dummy_item), dummy_item, dummy_nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> dummy_nbt, 'id' -> dummy_item}, dummy_nbt)), true));

    return(hopper);
);

fillItemFiltersFromItems(item_filter, from_pos, to_pos, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    fillItemFilters(item_filter, items, from_pos, to_pos);
);

fillItemFiltersFromItemList(item_filter, from_pos, to_pos, item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str(global_error_messages:'ITEM_LIST_EMPTY', item_list)));

    fillItemFilters(item_filter, items, from_pos, to_pos);
);

fillItemFiltersFromItemLayout(item_filter, from_pos, to_pos, item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = _readItemLayout(item_layout);
    if(!items, _error(str(global_error_messages:'ITEM_LAYOUT_EMPTY', item_layout)));

    fillItemFilters(item_filter, items, from_pos, to_pos);
);

fillItemFilters(item_filter, items, from_pos, to_pos) -> (
    if(!_isValidItemFilter(item_filter), _error(global_error_messages:'INVALID_ITEM_FILTER'));
    if(!items, _error(global_error_messages:'NO_ITEMS_PROVIDED'));
    if(length(filter(to_pos - from_pos, _ == 0)) < 2, _error(global_error_messages:'NOT_A_ROW_OF_BLOCKS'));

    _handleInvalidItems(items);
    _handleUnstackableItems(items);
    if(global_settings:'invalid_items_mode' == 'skip', items = filter(items, !_isInvalidItem(_:0) && !_isUnstackable(_:0)));

    fallback_item = if(
        global_settings:'invalid_items_mode' == 'dummy_item', global_stackable_dummy_item,
        global_settings:'invalid_items_mode' == 'air', ['air', null]
    );

    i = 0;
    affected_blocks = for(_scanStrip(from_pos, to_pos),
        block = _;

        if(block != 'hopper',
            if(!global_settings:'skip_empty_spots', i += 1);
            continue();
        );

        [item, nbt] = if(
            global_settings:'extra_containers_mode' == 'repeat' || length(items) == 1 || i < length(items), items:i,
            global_settings:'extra_containers_mode' == 'dummy_item', global_stackable_dummy_item,
            global_settings:'extra_containers_mode' == 'air', ['air', null],
            global_settings:'extra_containers_mode' == 'no_fill', continue()
        );

        i += 1;

        if(item == 'air' && global_settings:'air_mode' == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item) || _isUnstackable(item),
            if(!fallback_item, continue());
            [item, nbt] = fallback_item;
        );

        _itemFilter(block, item_filter, [item, nbt]);
        _updateComparators(block);
        true;
    );

    print(format('f » ', 'g Filled ', str('%s %s', global_color, affected_blocks), str('g  hopper%s with the ', if(affected_blocks == 1, '', 's')), str('%s %s', global_color, _formatItemFilter(item_filter)), 'g  item filter'));
    run('playsound block.note_block.pling master @s');
);

giveItemFilterFromItems(item_filter, item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    giveItemFilter(item_filter, items);
);

giveItemFilterFromItemList(item_filter, item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str(global_error_messages:'ITEM_LIST_EMPTY', item_list)));

    giveItemFilter(item_filter, items);
);

giveItemFilterFromItemLayout(item_filter, item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = _readItemLayout(item_layout);
    if(!items, _error(str(global_error_messages:'ITEM_LAYOUT_EMPTY', item_layout)));

    giveItemFilter(item_filter, items);
);

giveItemFilter(item_filter, items) -> (
    if(!_isValidItemFilter(item_filter), _error(global_error_messages:'INVALID_ITEM_FILTER'));
    if(!items, _error(global_error_messages:'NO_ITEMS_PROVIDED'));

    invalid_items = filter(map(items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str(global_error_messages:'INVALID_ITEMS', join(', ', sort(_removeDuplicates(invalid_items))))));
    unstackable_items = filter(map(items, _:0), _isUnstackable(_));
    if(unstackable_items, _error(str(global_error_messages:'CANT_USE_UNSTACKABLES', join(', ', sort(_removeDuplicates(unstackable_items))))));

    stx_data = {
        'item_filter' -> {
            'item_filter' -> item_filter,
            'items' -> map(items, [item, nbt] = _; {'item' -> item, ...if(nbt, {'nbt' -> nbt}, {})})
        }
    };
    item_maps = [];
    for(items,
        if(_i >= 27, break());
        [item, nbt] = _;
        if(item != 'air', item_maps += _itemToMap(_i, item, 1, nbt));
    );
    hopper_name = _formatTextComponent({'text' -> str('STX %s Item Filter', _formatItemFilter(item_filter)), 'color' -> global_color, 'bold' -> true, 'italic' -> false});
    hopper_lore = [_formatTextComponent([{'text' -> '» ', 'color' -> 'dark_gray', 'italic' -> false}, {'text' -> str(length(items)), 'color' -> global_color, 'italic' -> false}, {'text' -> str(' item%s', if(length(items) == 1, '', 's')), 'color' -> 'gray', 'italic' -> false}])];
    hopper_nbt = if(system_info('game_pack_version') >= 33,
        {
            'custom_data' -> {'stx' -> stx_data},
            'container' -> item_maps,
            'custom_name' -> hopper_name,
            'lore' -> hopper_lore,
            'enchantment_glint_override' -> true,
            if(system_info('game_pack_version') >= 64,
                'tooltip_display' -> {'hidden_components' -> ['container']},
                'hide_additional_tooltip' -> {}
            )
        },
        {
            'stx' -> stx_data,
            'BlockEntityTag' -> {'Items' -> item_maps},
            'display' -> {
                'Name' -> hopper_name,
                'Lore' -> hopper_lore
            },
            'Enchantments' -> [{}]
        }
    );

    run(_giveCommand('hopper', hopper_nbt));

    print(format('f » ', 'g You were given a ', str('%s %s', global_color, _formatItemFilter(item_filter)), str('g  item filter with %d item%s', length(items), if(length(items) == 1, '', 's'))));
);

__on_item_filter_placed(block, player_facing, data) -> (
    origin_pos = pos(block);

    hopper_facing = block_state(block, 'facing');
    item_filter = data:'item_filter';
    items = map(parse_nbt(data:'items'), [_:'item', _:'nbt']);

    _handleInvalidItems(items);
    _handleUnstackableItems(items);
    if(global_settings:'invalid_items_mode' == 'skip', items = filter(items, !_isInvalidItem(_:0) && !_isUnstackable(_:0)));

    fallback_item = if(
        global_settings:'invalid_items_mode' == 'dummy_item', global_stackable_dummy_item,
        global_settings:'invalid_items_mode' == 'air', ['air', null]
    );

    set(block, 'air');

    placed_blocks = for(items,
        pos = pos_offset(origin_pos, player_facing, _i * global_settings:'placing_offset');
        if(!global_settings:'replace_blocks' && _i != 0 && !air(pos), continue());

        set(pos, 'hopper', {'facing' -> hopper_facing});

        [item, nbt] = items:_i;

        if(item == 'air' && global_settings:'air_mode' == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item) || _isUnstackable(item),
            if(!fallback_item, continue());
            [item, nbt] = fallback_item;
        );

        _itemFilter(block(pos), item_filter, [item, nbt]);
        _updateComparators(pos);
        true;
    );

    print(format('f » ', 'g Placed and filled ', str('%s %s', global_color, placed_blocks), str('g  hopper%s with the ', if(placed_blocks == 1, '', 's')), str('%s %s', global_color, _formatItemFilter(item_filter)), 'g  item filter'));
    sound('entity.evoker.cast_spell', origin_pos);
);

viewItemFilter(item_filter, item_tuple) -> (
    if(!_isValidItemFilter(item_filter), _error(global_error_messages:'INVALID_ITEM_FILTER'));

    [item, count, nbt] = item_tuple || ['fern', null, null];
    if(system_info('game_pack_version') >= 33, nbt = nbt:'components');
    if(_isUnstackable(item), _error(global_error_messages:'ONLY_STACKABLES_ALLOWED'));

    screen = create_screen(player(), 'hopper', str('%s Item Filter', _formatItemFilter(item_filter)), _(screen, player, action, data) -> (
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
        mode == 'single_full_box', _singleFullBox(block, item_tuple)
    );
);

_emptyBoxes(block, count) -> (
    shulker_box = _getShulkerBoxString(global_settings:'shulker_box_color');
    loop(inventory_size(block), inventory_set(block, _, count || 1, shulker_box, if(system_info('game_pack_version') >= 33, {'components' -> {'max_stack_size' -> count || 1}, 'id' -> shulker_box})));
);

_full(block, item_tuple) -> (
    [item, nbt] = item_tuple;

    loop(inventory_size(block), inventory_set(block, _, stack_limit(item), item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true)));
);

_fullBoxes(block, item_tuple) -> (
    [item, nbt] = item_tuple;

    shulker_box = _getShulkerBoxString(global_settings:'shulker_box_color');
    shulker_box_nbt = _generateFullShulkerBox(item, nbt);
    loop(inventory_size(block), inventory_set(block, _, 1, shulker_box, encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> shulker_box_nbt, 'id' -> shulker_box}, shulker_box_nbt), true)));
);

_prefill(block, item_tuple) -> (
    [item, nbt] = item_tuple;
    [dummy_item, dummy_nbt] = global_stackable_dummy_item;

    inventory_set(block, 0, stack_limit(item), item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true));
    for(range(1, inventory_size(block)), inventory_set(block, _, 1, dummy_item, dummy_nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> dummy_nbt, 'id' -> dummy_item}, dummy_nbt), true)));
);

_prefillUnstackables(block, item_tuple) -> (
    [item, nbt] = item_tuple;
    [dummy_item, dummy_nbt] = global_unstackable_dummy_item;

    inventory_set(block, 0, stack_limit(item), item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true));
    for(range(1, inventory_size(block)), inventory_set(block, _, 1, ...global_unstackable_dummy_item));
);

_singleItem(block, item_tuple) -> (
    [item, nbt] = item_tuple;

    inventory_set(block, 0, 1, item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true));
    for(range(1, inventory_size(block)), inventory_set(block, _, 0));
);

_singleStack(block, item_tuple) -> (
    [item, nbt] = item_tuple;

    inventory_set(block, 0, stack_limit(item), item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true));
    for(range(1, inventory_size(block)), inventory_set(block, _, 0));
);

_singleFullBox(block, item_tuple) -> (
    [item, nbt] = item_tuple;

    shulker_box = _getShulkerBoxString(global_settings:'shulker_box_color');
    shulker_box_nbt = _generateFullShulkerBox(item, nbt);
    inventory_set(block, 0, 1, shulker_box, encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> shulker_box_nbt, 'id' -> shulker_box}, shulker_box_nbt), true));
    for(range(1, inventory_size(block)), inventory_set(block, _, 0));
);

fillContainersFromItems(from_pos, to_pos, item_string, mode) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    fillContainers(mode, items, from_pos, to_pos);
);

fillContainersFromItemList(from_pos, to_pos, item_list, mode) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str(global_error_messages:'ITEM_LIST_EMPTY', item_list)));

    fillContainers(mode, items, from_pos, to_pos);
);

fillContainersFromItemLayout(from_pos, to_pos, item_layout, mode) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = _readItemLayout(item_layout);
    if(!items, _error(str(global_error_messages:'ITEM_LAYOUT_EMPTY', item_layout)));

    fillContainers(mode, items, from_pos, to_pos);
);

_containerFillFeedbackMessage(mode, affected_blocks) -> (
    print(format([
        'f » ',
        str('g %s ', if(
            mode == 'full' || mode == 'full_boxes', 'Completely filled',
            mode == 'prefill' || mode == 'prefill_unstackables', 'Prefilled',
            mode == 'single_item' || mode == 'single_stack' || mode == 'single_full_box', 'Filled'
        )),
        str('%s %s', global_color, affected_blocks),
        str('g  container%s %s', if(affected_blocks == 1, '', 's'), if(
            mode == 'full_boxes', 'with full boxes',
            mode == 'prefill_unstackables', 'with unstackables',
            mode == 'full' || mode == 'prefill', '',
            mode == 'single_item', 'with single items',
            mode == 'single_stack', 'with single stacks',
            mode == 'single_full_box', 'with single full boxes'
        ))
    ]));
    run('playsound block.note_block.pling master @s');
);

fillContainers(mode, items, from_pos, to_pos) -> (
    if(global_container_modes~mode == null, _error(global_error_messages:'INVALID_CONTAINER_MODE'));
    if(!items, _error(global_error_messages:'NO_ITEMS_PROVIDED'));

    _handleInvalidItems(items);
    if(global_settings:'invalid_items_mode' == 'skip', items = filter(items, !_isInvalidItem(_:0)));

    fallback_item = if(
        global_settings:'invalid_items_mode' == 'dummy_item', global_stackable_dummy_item,
        global_settings:'invalid_items_mode' == 'air', ['air', null]
    );

    i = 0;
    affected_blocks = 0;
    for(_scanStripes(from_pos, to_pos),
        [item, nbt] = if(
            global_settings:'extra_containers_mode' == 'repeat' || length(items) == 1 || i < length(items), items:i,
            global_settings:'extra_containers_mode' == 'dummy_item', global_stackable_dummy_item,
            global_settings:'extra_containers_mode' == 'air', ['air', null],
            global_settings:'extra_containers_mode' == 'no_fill', continue()
        );

        if(item == 'air' && global_settings:'air_mode' == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
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

        if(!global_settings:'skip_empty_spots' || success_count > 0, i += 1);
    );

    _containerFillFeedbackMessage(mode, affected_blocks);
);

giveContainerFromItems(container, item_string, mode) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    giveContainer(mode, container, items);
);

giveContainerFromItemList(container, item_list, mode) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str(global_error_messages:'ITEM_LIST_EMPTY', item_list)));

    giveContainer(mode, container, items);
);

giveContainerFromItemLayout(container, item_layout, mode) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = _readItemLayout(item_layout);
    if(!items, _error(str(global_error_messages:'ITEM_LAYOUT_EMPTY', item_layout)));

    giveContainer(mode, container, items);
);

giveContainer(mode, container, items) -> (
    if(global_container_modes~mode == null, _error(global_error_messages:'INVALID_CONTAINER_MODE'));
    if(global_containers~container == null, _error(global_error_messages:'INVALID_CONTAINER_TYPE'));
    if(!items, _error(global_error_messages:'NO_ITEMS_PROVIDED'));

    invalid_items = filter(map(items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str(global_error_messages:'INVALID_ITEMS', join(', ', sort(_removeDuplicates(invalid_items))))));

    stx_data = {
        'container' -> {
            'mode' -> mode,
            ...if(container == 'double_chest', {'double_chest' -> true}, {}),
            'items' -> map(items, [item, nbt] = _; {'item' -> item, ...if(nbt, {'nbt' -> nbt}, {})})
        }
    };
    item_maps = [];
    for(items,
        if(_i >= 27, break());
        [item, nbt] = _;
        if(item != 'air', item_maps += _itemToMap(_i, item, 1, nbt));
    );
    container_name = _formatTextComponent({'text' -> str('STX %s Container', global_container_modes:mode || mode), 'color' -> global_color, 'bold' -> true, 'italic' -> false});
    container_lore = [_formatTextComponent([{'text' -> '» ', 'color' -> 'dark_gray', 'italic' -> false}, {'text' -> str(length(items)), 'color' -> global_color, 'italic' -> false}, {'text' -> str(' item%s', if(length(items) == 1, '', 's')), 'color' -> 'gray', 'italic' -> false}])];
    container_nbt = if(system_info('game_pack_version') >= 33,
        {
            'custom_data' -> {'stx' -> stx_data},
            'container' -> item_maps,
            'custom_name' -> container_name,
            'lore' -> container_lore,
            'enchantment_glint_override' -> true,
            if(system_info('game_pack_version') >= 64,
                'tooltip_display' -> {'hidden_components' -> ['container']},
                'hide_additional_tooltip' -> {}
            )
        },
        {
            'stx' -> stx_data,
            'BlockEntityTag' -> {'Items' -> item_maps},
            'display' -> {
                'Name' -> container_name,
                'Lore' -> container_lore
            },
            'Enchantments' -> [{}]
        }
    );

    run(_giveCommand(if(container == 'double_chest', 'chest', container), container_nbt));

    print(format('f » ', 'g You were given a ', str('%s %s', global_color, global_container_modes:mode || mode), str('g  container with %d item%s', length(items), if(length(items) == 1, '', 's'))));
);

__on_container_placed(block, player_facing, data) -> (
    origin_pos = pos(block);

    container_facing = block_state(block, 'facing');
    mode = data:'mode';
    items = map(parse_nbt(data:'items'), [_:'item', _:'nbt']);

    _handleInvalidItems(items);
    if(global_settings:'invalid_items_mode' == 'skip', items = filter(items, !_isInvalidItem(_:0)));

    fallback_item = if(
        global_settings:'invalid_items_mode' == 'dummy_item', global_stackable_dummy_item,
        global_settings:'invalid_items_mode' == 'air', ['air', null]
    );

    if(block == 'chest' && data:'double_chest',
        second_chest_offset = global_cardinal_directions:(global_cardinal_directions~container_facing + if(global_settings:'double_chest_direction' == 'left', 1, -1));
        second_chest_direction = if(global_settings:'double_chest_direction' == 'left', 'right', 'left');
    );

    set(block, 'air');

    placed_blocks = for(items,
        pos = pos_offset(origin_pos, player_facing, _i * global_settings:'placing_offset');
        if(!global_settings:'replace_blocks' && _i != 0 && !air(pos), continue());

        set(pos, str(block), if(container_facing, {'facing' -> container_facing}));

        if(block == 'chest' && data:'double_chest',
            pos1 = pos_offset(pos, second_chest_offset);
            if(global_settings:'replace_blocks' || air(pos1), set(pos1, 'chest', {'facing' -> container_facing, 'type' -> second_chest_direction}));
        );

        [item, nbt] = items:_i;

        if(item == 'air' && global_settings:'air_mode' == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item),
            if(!fallback_item, continue());
            [item, nbt] = fallback_item;
        );

        _container(block(pos), mode, [item, nbt]);
        _updateComparators(pos);
        true;
    );

    print(format('f » ', str('g Placed and filled %d ', placed_blocks), str('%s %s', global_color, global_container_modes:mode || mode), str('g  container%s', if(placed_blocks == 1, '', 's'))));
    sound('entity.evoker.cast_spell', origin_pos);
);

// QUICK FILL

quickFillItemFilter(item_filter, item_tuple, from_pos, to_pos) -> (
    if(!_isValidItemFilter(item_filter), _error(global_error_messages:'INVALID_ITEM_FILTER'));

    [item, count, nbt] = item_tuple;
    if(system_info('game_pack_version') >= 33, nbt = nbt:'components');
    if(_isUnstackable(item), _error(global_error_messages:'ONLY_STACKABLES_ALLOWED'));

    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error(global_error_messages:'NOT_LOOKING_AT_ANY_BLOCK'));
        from_pos = trace;
    );
    if(!to_pos,
        if(block(from_pos) != 'hopper', _error(global_error_messages:'NOT_A_HOPPER'));
        to_pos = from_pos;
    );

    affected_blocks = volume(from_pos, to_pos,
        block = _;

        if(block == 'hopper',
            _itemFilter(block, item_filter, [item, nbt]);
            _updateComparators(block);
            true;
        );
    );

    print(format('f » ', 'g Filled ', str('%s %s', global_color, affected_blocks), str('g  hopper%s with the ', if(affected_blocks == 1, '', 's')), str('%s %s', global_color, _formatItemFilter(item_filter)), 'g  item filter'));
    run('playsound block.note_block.pling master @s');
);

quickFillContainer(item_tuple, from_pos, to_pos, mode) -> (
    if(global_container_modes~mode == null, _error(global_error_messages:'INVALID_CONTAINER_MODE'));

    [item, count, nbt] = item_tuple;
    if(system_info('game_pack_version') >= 33, nbt = nbt:'components');
    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error(global_error_messages:'NOT_LOOKING_AT_ANY_BLOCK'));
        from_pos = trace;
    );
    if(!to_pos,
        if(inventory_has_items(from_pos) == null, _error(global_error_messages:'NOT_A_CONTAINER'));
        to_pos = from_pos;
    );

    affected_blocks = volume(from_pos, to_pos,
        block = _;

        if(inventory_has_items(block) != null,
            _container(block, mode, [item, nbt]);
            _updateComparators(block);
            true;
        );
    );

    _containerFillFeedbackMessage(mode, affected_blocks);
);

quickFillEmptyBoxes(from_pos, to_pos, count) -> (
    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error(global_error_messages:'NOT_LOOKING_AT_ANY_BLOCK'));
        from_pos = trace;
    );
    if(!to_pos,
        if(inventory_has_items(from_pos) == null, _error(global_error_messages:'NOT_A_CONTAINER'));
        to_pos = from_pos;
    );

    affected_blocks = volume(from_pos, to_pos,
        block = _;

        if(inventory_has_items(block) != null,
            _emptyBoxes(block, count);
            _updateComparators(block);
            true;
        );
    );

    print(format('f » ', 'g Filled ', str('%s %s', global_color, affected_blocks), str('g  container%s with %sempty boxes', if(affected_blocks == 1, '', 's'), if(count == 1, '', 'stacked '))));
    run('playsound block.note_block.pling master @s');
);

// EMPTY CONTAINERS

_empty(block) -> (
    loop(inventory_size(block), inventory_set(block, _, 0));
);

fillContainersEmpty(from_pos, to_pos, block_predicate) -> (
    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error(global_error_messages:'NOT_LOOKING_AT_ANY_BLOCK'));
        from_pos = trace;
    );
    if(!to_pos,
        if(inventory_has_items(from_pos) == null, _error(global_error_messages:'NOT_A_CONTAINER'));
        to_pos = from_pos;
    );

    affected_blocks = volume(from_pos, to_pos,
        block = _;

        if(inventory_has_items(block) != null && (!block_predicate || _checkPredicate(block, block_predicate)),
            _empty(block);
            _updateComparators(block);
            true;
        );
    );

    print(format('f » ', 'g Emptied ', str('%s %s', global_color, affected_blocks), str('g  container%s', if(affected_blocks == 1, '', 's'))));
    run('playsound block.note_block.pling master @s');
);

// REDYE BOXES

_redyeBoxes(block, color) -> (
    shulker_box = _getShulkerBoxString(color);

    if(inventory_has_items(block) != null,
        affected_boxes = for(range(inventory_size(block)),
            i = inventory_get(block, _i);
            if(!i, continue());
            [item, count, nbt] = i;
            if(item~'shulker_box' == null || item == shulker_box, continue());
            if(system_info('game_pack_version') >= 33, nbt:'id' = shulker_box);
            inventory_set(block, _i, count, shulker_box, nbt);
        );
        return(affected_boxes);
    );
);

redyeBoxes(color, from_pos, to_pos) -> (
    if(global_shulker_box_colors~color == null, _error(global_error_messages:'INVALID_SHULKER_BOX_COLOR'));

    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error(global_error_messages:'NOT_LOOKING_AT_ANY_BLOCK'));
        from_pos = trace;
    );
    if(!to_pos,
        if(inventory_has_items(from_pos) == null, _error(global_error_messages:'NOT_A_CONTAINER'));
        to_pos = from_pos;
    );


    affected_boxes = 0;
    affected_blocks = volume(from_pos, to_pos,
        block = _;

        x = _redyeBoxes(block, color);
        if(x > 0, affected_boxes += x);
    );

    print(format('f » ', 'g Redyed ', str('%s %s', global_color, affected_boxes), str('g  shulker box%s', if(affected_blocks == 1, '', 'es')), 'g  in ', str('%s %s', global_color, affected_blocks), str('g  container%s', if(affected_blocks == 1, '', 's'))));
    run('playsound block.note_block.pling master @s');
);

// MIS CHEST

_MISChest(chest, items) -> (
    fallback_item = if(
        global_settings:'invalid_items_mode' == 'dummy_item', global_stackable_dummy_item,
        global_settings:'invalid_items_mode' == 'air', ['air', null]
    );

    skipped_items = if(length(items) > 54, slice(items, 54), []);
    loop(inventory_size(chest),
        if(_ >= length(items),
            inventory_set(chest, _, 1, ...global_unstackable_dummy_item);
            continue();
        );

        [item, nbt] = items:_;

        if(item == 'air' && global_settings:'air_mode' == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item) || _isUnstackable(item),
            if(!fallback_item, continue());
            [item, nbt] = fallback_item;
        );

        inventory_set(chest, _, 2, item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true));
    );

    return(skipped_items);
);

fillMISChests(from_pos, to_pos, item_list_string) -> (
    if(length(filter(to_pos - from_pos, _ == 0)) < 2, _error(global_error_messages:'NOT_A_ROW_OF_BLOCKS'));

    item_lists = split(' ', item_list_string);
    if(!item_lists, _error(global_error_messages:'NO_ITEM_LISTS_PROVIDED'));

    invalid_item_lists = filter(item_lists, list_files('item_lists', 'shared_text')~str('item_lists/%s', _) == null);
    if(invalid_item_lists, _error(str(global_error_messages:'INVALID_ITEM_LISTS', join(', ', sort(_removeDuplicates(invalid_item_lists))))));

    item_list_contents = map(item_lists, _readItemList(_));
    all_items = reduce(item_list_contents, [..._a, ..._], []);

    _handleInvalidItems(all_items);
    _handleUnstackableItems(all_items);

    i = 0;
    affected_blocks = for(_scanStrip(from_pos, to_pos),
        block = _;

        if(block != 'chest',
            if(!global_settings:'skip_empty_spots', i += 1);
            continue();
        );
        if(i >= length(item_lists), continue());

        items = item_list_contents:i;
        if(global_settings:'invalid_items_mode' == 'skip', items = filter(items, !_isInvalidItem(_:0) && !_isUnstackable(_:0)));

        i += 1;

        skipped_items = _MISChest(block, items);
        if(skipped_items, print(format('f » ', 'rb WARNING!', str('g  Not enough space in the MIS chest at %s for %d items: %s', pos(block), length(skipped_items), join(', ', map(skipped_items, _:0))))));
        true;
    );

    print(format('f » ', 'g Filled ', str('%s %s', global_color, affected_blocks), str('g  MIS Chest%s', if(affected_blocks == 1, '', 's'))));
    run('playsound block.note_block.pling master @s');
);

giveMISChest(item_list_string) -> (
    item_lists = split(' ', item_list_string);
    if(!item_lists, _error(global_error_messages:'NO_ITEM_LISTS_PROVIDED'));

    invalid_item_lists = filter(item_lists, list_files('item_lists', 'shared_text')~str('item_lists/%s', _) == null);
    if(invalid_item_lists, _error(str(global_error_messages:'INVALID_ITEM_LISTS', join(', ', sort(_removeDuplicates(invalid_item_lists))))));

    item_list_contents = map(item_lists, _readItemList(_));
    all_items = reduce(item_list_contents, [..._a, ..._], []);

    invalid_items = filter(map(all_items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str(global_error_messages:'INVALID_ITEMS', join(', ', sort(_removeDuplicates(invalid_items))))));
    unstackable_items = filter(map(all_items, _:0), _isUnstackable(_));
    if(unstackable_items, _error(str(global_error_messages:'CANT_USE_UNSTACKABLES', join(', ', sort(_removeDuplicates(unstackable_items))))));

    stx_data = {
        'mis_chest' -> {
            'items' -> map(item_list_contents, map(_, [item, nbt] = _; {'item' -> item, ...if(nbt, {'nbt' -> nbt}, {})}))
        }
    };
    chest_name = _formatTextComponent({'text' -> 'STX MIS Chest', 'color' -> global_color, 'bold' -> true, 'italic' -> false});
    chest_lore = [_formatTextComponent([{'text' -> '» ', 'color' -> 'dark_gray', 'italic' -> false}, {'text' -> str(length(item_lists)), 'color' -> global_color, 'italic' -> false}, {'text' -> str(' entr%s', if(length(item_lists) == 1, 'y', 'ies')), 'color' -> 'gray', 'italic' -> false}])];
    chest_nbt = if(system_info('game_pack_version') >= 33,
        {
            'custom_data' -> {'stx' -> stx_data},
            'custom_name' -> chest_name,
            'lore' -> chest_lore,
            'enchantment_glint_override' -> true,
            if(system_info('game_pack_version') >= 64,
                'tooltip_display' -> {'hidden_components' -> ['container']},
                'hide_additional_tooltip' -> {}
            )
        },
        {
            'stx' -> stx_data,
            'display' -> {
                'Name' -> chest_name,
                'Lore' -> chest_lore
            },
            'Enchantments' -> [{}]
        }
    );

    run(_giveCommand('chest', chest_nbt));

    print(format('f » ', 'g You were given a ', str('%s MIS Chest', global_color), str('g  with %d entr%s', length(item_lists), if(length(item_lists) == 1, 'y', 'ies'))));
);

__on_MIS_chest_placed(block, player_facing, data) -> (
    origin_pos = pos(block);

    chest_facing = block_state(block, 'facing');
    chest_contents = map(parse_nbt(data:'items'), map(_, [_:'item', _:'nbt']));
    all_items = reduce(chest_contents, [..._a, ..._], []);

    _handleInvalidItems(all_items);
    _handleUnstackableItems(all_items);

    second_chest_offset = global_cardinal_directions:(global_cardinal_directions~chest_facing + if(global_settings:'double_chest_direction' == 'left', 1, -1));
    second_chest_direction = if(global_settings:'double_chest_direction' == 'left', 'right', 'left');

    set(block, 'air');

    placed_blocks = for(chest_contents,
        pos = pos_offset(origin_pos, player_facing, _i * global_settings:'placing_offset');
        pos1 = pos_offset(pos, second_chest_offset);
        if(!global_settings:'replace_blocks' && _i != 0 && !air(pos), continue());

        set(pos, 'chest', {'facing' -> chest_facing});
        if(global_settings:'replace_blocks' || air(pos1), set(pos1, 'chest', {'facing' -> chest_facing, 'type' -> second_chest_direction}));

        items = chest_contents:_i;
        if(global_settings:'invalid_items_mode' == 'skip', items = filter(items, !_isInvalidItem(_:0) && !_isUnstackable(_:0)));

        skipped_items = _MISChest(block(pos), items);
        if(skipped_items, print(format('f » ', 'rb WARNING!', str('g  Not enough space in the MIS chest at %s for %d items: %s', pos(block), length(skipped_items), join(', ', map(skipped_items, _:0))))));
        true;
    );

    print(format('f » ', 'g Placed and filled ', str('%s %s', global_color, placed_blocks), str('g  MIS Chest%s', if(placed_blocks == 1, '', 's'))));
    sound('entity.evoker.cast_spell', origin_pos);
);

// ENCODER CHEST

_encoderChest(chest, items, signal_strength) -> (
    amount = ceil(inventory_size(chest) * 64 / 14 * signal_strength);

    i = -1;
    loop(inventory_size(chest),
        if(inventory_size(chest) - _ - 1 < amount / 64,
            count = min(amount, 64);
            [item, nbt] = if(
                global_settings:'encoder_chest_max_filling' && _ < length(items),
                    items:(i+=1),
                global_settings:'prefer_unstackables' && count == 64,
                    global_unstackable_dummy_item,
                // else
                    global_stackable_dummy_item
            );
            inventory_set(chest, _, count / (64 / stack_limit(item)), item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true));
            amount += -count;
            continue();
        );

        if(
            _ < length(items),
                [item, nbt] = items:(i+=1),
            global_settings:'encoder_chest_fill_all_slots',
                [item, nbt] = global_stackable_dummy_item,
            //else
                continue()
        );

        if((item == 'air' && global_settings:'air_mode' == 'dummy_item') || _isInvalidItem(item) || _isUnstackable(item), [item, nbt] = global_stackable_dummy_item);

        inventory_set(chest, _, global_settings:'encoder_chest_item_amount', item, nbt && encode_nbt(if(system_info('game_pack_version') >= 33, {'components' -> nbt, 'id' -> item}, nbt), true));

        if(item != 'air', amount += -global_settings:'encoder_chest_item_amount' * (64 / stack_limit(item)));
    );
    skipped_items = if(i+1 < length(items), slice(items, i+1), []);

    return(skipped_items);
);

fillEncoderChests(signal_strength, from_pos, to_pos, item_list_string) -> (
    if(length(filter(to_pos - from_pos, _ == 0)) < 2, _error(global_error_messages:'NOT_A_ROW_OF_BLOCKS'));

    item_lists = split(' ', item_list_string);
    if(!item_lists, _error(global_error_messages:'NO_ITEM_LISTS_PROVIDED'));

    invalid_item_lists = filter(item_lists, list_files('item_lists', 'shared_text')~str('item_lists/%s', _) == null);
    if(invalid_item_lists, _error(str(global_error_messages:'INVALID_ITEM_LISTS', join(', ', sort(_removeDuplicates(invalid_item_lists))))));

    item_list_contents = map(item_lists, _readItemList(_));
    all_items = reduce(item_list_contents, [..._a, ..._], []);

    _handleInvalidItems(all_items);
    _handleUnstackableItems(all_items);

    i = 0;
    affected_blocks = for(_scanStrip(from_pos, to_pos),
        block = _;

        if(block != 'chest' && block != 'barrel',
            if(!global_settings:'skip_empty_spots', i += 1);
            continue();
        );
        if(i >= length(item_lists), continue());

        items = item_list_contents:i;
        if(global_settings:'invalid_items_mode' == 'skip', items = filter(items, !_isInvalidItem(_:0) && !_isUnstackable(_:0)));

        i += 1;

        skipped_items = _encoderChest(block, items, signal_strength);
        if(skipped_items, print(format('f » ', 'rb WARNING!', str('g  Not enough space in the encoder chest at %s for %d items: %s', pos(block), length(skipped_items), join(', ', map(skipped_items, _:0))))));
        true;
    );

    print(format('f » ', 'g Filled ', str('%s %s', global_color, affected_blocks), str('g  Encoder Chest%s', if(affected_blocks == 1, '', 's'))));
    run('playsound block.note_block.pling master @s');
);

fixEncoderChests(signal_strength, from_pos, to_pos) -> (
    trace = query(player(), 'trace', 5, 'blocks');
    if(!from_pos,
        if(!trace, _error(global_error_messages:'NOT_LOOKING_AT_ANY_BLOCK'));
        from_pos = trace;
    );
    if(!to_pos,
        if(inventory_has_items(from_pos) == null, _error(global_error_messages:'NOT_A_CONTAINER'));
        to_pos = from_pos;
    );

    affected_blocks = volume(from_pos, to_pos,
        block = _;
        if(block != 'chest' && block != 'barrel', continue());

        items = filter(inventory_get(block), _ && !_isUnstackable(_:0));
        items = map(items, [_:0, if(system_info('game_pack_version') >= 33, _:2:'components', _:2)]);
        items = filter(items, !has(_:1, if(system_info('game_pack_version') >= 33, 'minecraft:custom_name', 'display.Name')));
        items = _removeDuplicates(items);

        if(!length(items), continue());

        _encoderChest(block, items, signal_strength);
        true;
    );

    print(format('f » ', 'g Fixed ', str('%s %s', global_color, affected_blocks), str('g  Encoder Chest%s', if(affected_blocks == 1, '', 's'))));
    run('playsound block.note_block.pling master @s');
);

giveEncoderChest(signal_strength, item_list_string) -> (
    item_lists = split(' ', item_list_string);
    if(!item_lists, _error(global_error_messages:'NO_ITEM_LISTS_PROVIDED'));

    invalid_item_lists = filter(item_lists, list_files('item_lists', 'shared_text')~str('item_lists/%s', _) == null);
    if(invalid_item_lists, _error(str(global_error_messages:'INVALID_ITEM_LISTS', join(', ', sort(_removeDuplicates(invalid_item_lists))))));

    item_list_contents = map(item_lists, _readItemList(_));
    all_items = reduce(item_list_contents, [..._a, ..._], []);

    invalid_items = filter(map(all_items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str(global_error_messages:'INVALID_ITEMS', join(', ', sort(_removeDuplicates(invalid_items))))));
    unstackable_items = filter(map(all_items, _:0), _isUnstackable(_));
    if(unstackable_items, _error(str(global_error_messages:'CANT_USE_UNSTACKABLES', join(', ', sort(_removeDuplicates(unstackable_items))))));

    stx_data = {
        'encoder_chest' -> {
            'signal_strength' -> signal_strength,
            'items' -> map(item_list_contents, map(_, [item, nbt] = _; {'item' -> item, ...if(nbt, {'nbt' -> nbt}, {})}))
        }
    };
    chest_name = _formatTextComponent({'text' -> 'STX Encoder Chest', 'color' -> global_color, 'bold' -> true, 'italic' -> false});
    chest_lore = [_formatTextComponent([{'text' -> '» ', 'color' -> 'dark_gray', 'italic' -> false}, {'text' -> 'Signal strength ', 'color' -> 'gray', 'italic' -> false}, {'text' -> str(signal_strength), 'color' -> global_color, 'italic' -> false}]), _formatTextComponent([{'text' -> '» ', 'color' -> 'dark_gray', 'italic' -> false}, {'text' -> str(length(item_lists)), 'color' -> global_color, 'italic' -> false}, {'text' -> str(' entr%s', if(length(item_lists) == 1, 'y', 'ies')), 'color' -> 'gray', 'italic' -> false}])];
    chest_nbt = if(system_info('game_pack_version') >= 33,
        {
            'custom_data' -> {'stx' -> stx_data},
            'custom_name' -> chest_name,
            'lore' -> chest_lore,
            'enchantment_glint_override' -> true,
            if(system_info('game_pack_version') >= 64,
                'tooltip_display' -> {'hidden_components' -> ['container']},
                'hide_additional_tooltip' -> {}
            )
        },
        {
            'stx' -> stx_data,
            'display' -> {
                'Name' -> chest_name,
                'Lore' -> chest_lore
            },
            'Enchantments' -> [{}]
        }
    );

    run(_giveCommand('chest', chest_nbt));

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

    second_chest_offset = global_cardinal_directions:(global_cardinal_directions~chest_facing + if(global_settings:'double_chest_direction' == 'left', 1, -1));
    second_chest_direction = if(global_settings:'double_chest_direction' == 'left', 'right', 'left');

    set(block, 'air');

    placed_blocks = for(chest_contents,
        pos = pos_offset(origin_pos, player_facing, _i * global_settings:'placing_offset');
        pos1 = pos_offset(pos, second_chest_offset);
        if(!global_settings:'replace_blocks' && _i != 0 && !air(pos), continue());

        set(pos, 'chest', {'facing' -> chest_facing});
        if(global_settings:'replace_blocks' || air(pos1), set(pos1, 'chest', {'facing' -> chest_facing, 'type' -> second_chest_direction}));

        items = chest_contents:_i;
        if(global_settings:'invalid_items_mode' == 'skip', items = filter(items, !_isInvalidItem(_:0) && !_isUnstackable(_:0)));

        skipped_items = _encoderChest(block(pos), items, signal_strength);
        if(skipped_items, print(format('f » ', 'rb WARNING!', str('g  Not enough space in the encoder chest at %s for %d items: %s', pos(block), length(skipped_items), join(', ', map(skipped_items, _:0))))));
        true;
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
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str(global_error_messages:'ITEM_LIST_EMPTY', item_list)));

    placeItemFrames(items, from_pos, to_pos, facing);
);

placeItemFramesFromItemLayout(from_pos, to_pos, facing, item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = _readItemLayout(item_layout);
    if(!items, _error(str(global_error_messages:'ITEM_LAYOUT_EMPTY', item_layout)));

    placeItemFrames(items, from_pos, to_pos, facing);
);

placeItemFrames(items, from_pos, to_pos, item_frame_facing) -> (
    if(!items, _error(global_error_messages:'NO_ITEMS_PROVIDED'));
    if(global_directions~item_frame_facing == null, _error(global_error_messages:'INVALID_DIRECTION'));
    if(length(filter(to_pos - from_pos, _ == 0)) < 2, _error(global_error_messages:'NOT_A_ROW_OF_BLOCKS'));

    from_pos = pos_offset(from_pos, item_frame_facing);
    to_pos = pos_offset(to_pos, item_frame_facing);

    _handleInvalidItems(items);
    if(global_settings:'invalid_items_mode' == 'skip', items = filter(items, !_isInvalidItem(_:0)));

    fallback_item = if(
        global_settings:'invalid_items_mode' == 'dummy_item', global_stackable_dummy_item,
        global_settings:'invalid_items_mode' == 'air', ['air', null]
    );

    i = 0;
    spawned_entities = for(_scanStrip(from_pos, to_pos),

        [item, nbt] = if(
            global_settings:'extra_containers_mode' == 'repeat' || length(items) == 1 || i < length(items), items:i,
            global_settings:'extra_containers_mode' == 'dummy_item', global_stackable_dummy_item,
            global_settings:'extra_containers_mode' == 'air', ['air', null],
            global_settings:'extra_containers_mode' == 'no_fill', continue()
        );

        if(item == 'air' && global_settings:'air_mode' == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item),
            if(!fallback_item,
                i += 1;
                continue();
            );
            [item, nbt] = fallback_item;
        );

        spawn(global_settings:'item_frame_type', _, {'Facing' -> global_directions~item_frame_facing, 'Item' -> if(system_info('game_pack_version') >= 33, {'id' -> item, 'components' -> nbt}, {'id' -> item, 'Count' -> 1, ...if(nbt, {'tag' -> nbt}, {})})});

        i += 1;
    );

    print(format('f » ', 'g Placed ', str('%s %s', global_color, spawned_entities), str('g  item frame%s', if(spawned_entities == 1, '', 's'))));
    run('playsound block.note_block.pling master @s');
);

giveItemFramesFromItems(item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    giveItemFrames(items);
);

giveItemFramesFromItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str(global_error_messages:'ITEM_LIST_EMPTY', item_list)));

    giveItemFrames(items);
);

giveItemFramesFromItemLayout(item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = _readItemLayout(item_layout);
    if(!items, _error(str(global_error_messages:'ITEM_LAYOUT_EMPTY', item_layout)));

    giveItemFrames(items);
);

giveItemFrames(items) -> (
    if(!items, _error(global_error_messages:'NO_ITEMS_PROVIDED'));

    invalid_items = filter(map(items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str(global_error_messages:'INVALID_ITEMS', join(', ', sort(_removeDuplicates(invalid_items))))));

    stx_data = {
        'item_frame' -> {
            'items' -> map(items, [item, nbt] = _; {'item' -> item, ...if(nbt, {'nbt' -> nbt}, {})})
        }
    };
    item_maps = [];
    for(items,
        if(_i >= 27, break());
        [item, nbt] = _;
        if(item != 'air', item_maps += _itemToMap(_i, item, 1, nbt));
    );
    item_frame_name = _formatTextComponent({'text' -> 'STX Item Frame', 'color' -> global_color, 'bold' -> true, 'italic' -> false});
    item_frame_lore = [_formatTextComponent([{'text' -> '» ', 'color' -> 'dark_gray', 'italic' -> false}, {'text' -> str(length(items)), 'color' -> global_color, 'italic' -> false}, {'text' -> str(' item%s', if(length(items) == 1, '', 's')), 'color' -> 'gray', 'italic' -> false}])];
    item_frame_nbt = if(system_info('game_pack_version') >= 33,
        {
            'custom_data' -> {'stx' -> stx_data},
            'container' -> item_maps,
            'custom_name' -> item_frame_name,
            'lore' -> item_frame_lore,
            'enchantment_glint_override' -> true,
            if(system_info('game_pack_version') >= 64,
                'tooltip_display' -> {'hidden_components' -> ['container']},
                'hide_additional_tooltip' -> {}
            )
        },
        {
            'stx' -> stx_data,
            'BlockEntityTag' -> {'Items' -> item_maps},
            'display' -> {
                'Name' -> item_frame_name,
                'Lore' -> item_frame_lore
            },
            'Enchantments' -> [{}]
        }
    );

    run(_giveCommand(global_settings:'item_frame_type', item_frame_nbt));

    print(format('f » ', str('g You were given an item frame with %d item%s', length(items), if(length(items) == 1, '', 's'))));
);

__on_item_frame_placed(item_frame_type, origin_pos, item_frame_facing, player_facing, data) -> (
    items = map(parse_nbt(data:'items'), [_:'item', _:'nbt']);

    _handleInvalidItems(items);
    if(global_settings:'invalid_items_mode' == 'skip', items = filter(items, !_isInvalidItem(_:0)));

    fallback_item = if(
        global_settings:'invalid_items_mode' == 'dummy_item', global_stackable_dummy_item,
        global_settings:'invalid_items_mode' == 'air', ['air', null]
    );

    spawned_item_frames = for(items,
        pos = pos_offset(origin_pos, player_facing, _i * global_settings:'placing_offset');

        [item, nbt] = items:_i;

        if(item == 'air' && global_settings:'air_mode' == 'dummy_item', [item, nbt] = global_stackable_dummy_item);
        if(_isInvalidItem(item),
            if(!fallback_item, continue());
            [item, nbt] = fallback_item;
        );

        spawn(item_frame_type, pos, encode_nbt({'Facing' -> global_directions~item_frame_facing, 'Item' -> if(system_info('game_pack_version') >= 33, {'id' -> item, 'components' -> nbt}, {'id' -> item, 'Count' -> 1, 'tag' -> nbt})}, true));
    );

    print(format('f » ', 'g Placed ', str('%s %s', global_color, spawned_item_frames), str('g  item frame%s', if(spawned_item_frames == 1, '', 's'))));
    sound('entity.evoker.cast_spell', origin_pos);
);

// CHESTS

giveMixedChestsFromItems(item_string, mode) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    giveMixedChests(items, mode);
);

giveMixedChestsFromItemList(item_list, mode) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str(global_error_messages:'ITEM_LIST_EMPTY', item_list)));

    giveMixedChests(items, mode);
);

giveMixedChestsFromItemLayout(item_layout, mode) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = _readItemLayout(item_layout);
    if(!items, _error(str(global_error_messages:'ITEM_LAYOUT_EMPTY', item_layout)));

    giveMixedChests(items, mode);
);

giveMixedChests(items, mode) -> (
    if(!items, _error(global_error_messages:'NO_ITEMS_PROVIDED'));

    invalid_items = filter(map(items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str(global_error_messages:'INVALID_ITEMS', join(', ', sort(_removeDuplicates(invalid_items))))));

    shulker_box = _getShulkerBoxString(global_settings:'shulker_box_color');
    if(mode == 'mixed_boxes',
        items = map(range(length(items) / 27),
            item_group = slice(items, _ * 27, min(length(items), (_ + 1) * 27));
            item_maps = map(item_group, [item, nbt] = _; _itemToMap(_i, item, 1, nbt));
            [shulker_box, if(system_info('game_pack_version') >= 33, {'container' -> item_maps}, {'BlockEntityTag' -> {'Items' -> item_maps}})];
        );
    );
    chest_amount = ceil(length(items) / 27);
    loop(chest_amount,
        item_group = slice(items, _ * 27, min(length(items), (_ + 1) * 27));
        item_maps = map(item_group,
            [item, nbt] = _;
            if(
                mode == 'single_items' || mode == 'mixed_boxes',
                    _itemToMap(_i, item, 1, nbt),
                mode == 'single_stacks',
                    _itemToMap(_i, item, stack_limit(item), nbt),
                mode == 'single_item_boxes',
                    _itemToMap(_i, shulker_box, 1, if(system_info('game_pack_version') >= 33, {'container' -> [_itemToMap(0, item, 1, nbt)]}, {'BlockEntityTag' -> {'Items' -> [_itemToMap(0, item, 1, nbt)]}})),
                mode == 'single_stack_boxes',
                    _itemToMap(_i, shulker_box, 1, if(system_info('game_pack_version') >= 33, {'container' -> [_itemToMap(0, item, stack_limit(item), nbt)]}, {'BlockEntityTag' -> {'Items' -> [_itemToMap(0, item, stack_limit(item), nbt)]}})),
                mode == 'full_boxes',
                    _itemToMap(_i, shulker_box, 1, _generateFullShulkerBox(item, nbt))
            );
        );
        chest_name = _formatTextComponent({'text' -> str('%s Chest #%d', global_mixed_chest_modes:mode || mode, _ + 1), 'color' -> global_color, 'bold' -> true, 'italic' -> false});
        chest_nbt = if(system_info('game_pack_version') >= 33,
            {
                'container' -> item_maps,
                'custom_name' -> chest_name,
                if(system_info('game_pack_version') >= 64,
                    'tooltip_display' -> {'hidden_components' -> ['container']},
                    'hide_additional_tooltip' -> {}
                )
            },
            {
                'BlockEntityTag' -> {'Items' -> item_maps},
                'display' -> {'Name' -> chest_name}
            }
        );

        run(_giveCommand('chest', chest_nbt));
    );

    print(format('f » ', str('g You were given %d ', chest_amount), str('%s %s', global_color, global_mixed_chest_modes:mode || mode), str('g  chest%s', if(chest_amount == 1, '', 's'))));
);

// BUNDLES

giveBundleFromItems(item_string) -> (
    items = map(split(' ', item_string), [lower(_), null]);

    giveBundle(items);
);

giveBundleFromItemList(item_list) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    if(!items, _error(str(global_error_messages:'ITEM_LIST_EMPTY', item_list)));

    giveBundle(items);
);

giveBundleFromItemLayout(item_layout) -> (
    item_layout_path = str('item_layouts/%s', item_layout);
    if(list_files('item_layouts', 'shared_text')~item_layout_path == null, _error(str(global_error_messages:'ITEM_LAYOUT_DOESNT_EXIST', item_layout)));

    items = _readItemLayout(item_layout);
    if(!items, _error(str(global_error_messages:'ITEM_LAYOUT_EMPTY', item_layout)));

    giveBundle(items);
);

giveBundle(items) -> (
    if(!items, _error(global_error_messages:'NO_ITEMS_PROVIDED'));

    invalid_items = filter(map(items, _:0), _isInvalidItem(_));
    if(invalid_items, _error(str(global_error_messages:'INVALID_ITEMS', join(', ', sort(_removeDuplicates(invalid_items))))));

    item_maps = map(items, [item, nbt] = _; _itemToMap(_i, item, 1, nbt));
    bundle_nbt = if(system_info('game_pack_version') >= 33, {'bundle_contents' -> map(item_maps, _:'item')}, {'Items' -> item_maps});

    run(_giveCommand('bundle', bundle_nbt));
    print(format('f » ', 'g You were given a bundle with ', str('%s %d', global_color, length(items)), str('g  item%s', if(length(items) == 1, '', 's'))));
);

// MISSING ITEMS

missingItemsFromItemLists(item_list, item_list_string) -> (
    item_lists = split(' ', item_list_string);
    if(!item_lists, _error(global_error_messages:'NO_ITEM_LISTS_PROVIDED'));

    invalid_item_lists = filter(item_lists, list_files('item_lists', 'shared_text')~str('item_lists/%s', _) == null);
    if(invalid_item_lists, _error(str(global_error_messages:'INVALID_ITEM_LISTS', join(', ', sort(_removeDuplicates(invalid_item_lists))))));

    item_list_contents = map(item_lists, _readItemList(_));
    all_items = reduce(item_list_contents, [..._a, ..._], []);

    missingItems(item_list, all_items);
);

missingItemsFromItemLayouts(item_list, item_layout_string) -> (
    item_layouts = split(' ', item_layout_string);
    if(!item_layouts, _error(global_error_messages:'NO_ITEM_LAYOUTS_PROVIDED'));

    invalid_item_layouts = filter(item_layouts, list_files('item_layouts', 'shared_text')~str('item_layouts/%s', _) == null);
    if(invalid_item_layouts, _error(str(global_error_messages:'INVALID_ITEM_LAYOUTS', join(', ', sort(_removeDuplicates(invalid_item_layouts))))));

    item_layout_contents = map(item_layouts, _readItemLayout(_));
    all_items = reduce(item_layout_contents, [..._a, ..._], []);

    missingItems(item_list, all_items);
);

missingItems(item_list, current_items) -> (
    item_list_path = str('item_lists/%s', item_list);
    if(list_files('item_lists', 'shared_text')~item_list_path == null, _error(str(global_error_messages:'ITEM_LIST_DOESNT_EXIST', item_list)));

    items = _readItemList(item_list);
    missing_items = filter(items, current_items~_ == null);
    _itemScreen(missing_items, 'Missing Items');

    print(format('f » ', 'g Showing ', str('%s %d', global_color, length(missing_items)), str('g  missing item%s', if(length(missing_items) == 1, '', 's'))));
);

// FULL BOXES

getFullBox(item_tuple, shulker_box_color) -> (
    [item, count, nbt] = item_tuple;
    if(system_info('game_pack_version') >= 33, nbt = nbt:'components');

    shulker_box = _getShulkerBoxString(shulker_box_color || global_settings:'shulker_box_color');
    shulker_box_nbt = _generateFullShulkerBox(item, nbt);

    run(_giveCommand(shulker_box, shulker_box_nbt));
);

// EVENTS

__on_player_places_block(player, item_tuple, hand, block) -> (
    [item, count, nbt] = item_tuple;
    data = if(system_info('game_pack_version') >= 33, nbt:'components':'minecraft:custom_data', nbt);
    player_facing = _getFacing(player);
    if(
        item == 'hopper' && has(data, 'stx.item_filter'),
            __on_item_filter_placed(block, player_facing, data:'stx':'item_filter'),
        item == 'chest' && has(data, 'stx.mis_chest'),
            __on_MIS_chest_placed(block, player_facing, data:'stx':'mis_chest'),
        item == 'chest' && has(data, 'stx.encoder_chest'),
            __on_encoder_chest_placed(block, player_facing, data:'stx':'encoder_chest'),
        global_containers~item != null && has(data, 'stx.container'),
            __on_container_placed(block, player_facing, data:'stx':'container')
    );
);

__on_player_right_clicks_block(player, item_tuple, hand, block, face, hitvec) -> (
    if(!item_tuple, return());

    [item, count, nbt] = item_tuple;
    data = if(system_info('game_pack_version') >= 33, nbt:'components':'minecraft:custom_data', nbt);
    if((item != 'item_frame' && item != 'glow_item_frame') || !has(data, 'stx.item_frame'), return());

    item_frame_type = item;
    origin_pos = pos_offset(block, face);
    player_facing = _getFacing(player);
    __on_item_frame_placed(item_frame_type, origin_pos, face, player_facing, data:'stx':'item_frame');

    return('cancel');
);

saveConfig() -> (
    data = {
        if(
            global_stackable_dummy_item != global_default_stackable_dummy_item,
                'stackable_dummy_item' -> _itemToString(global_stackable_dummy_item),
            // else
                ...{}
        ),
        if(
            global_unstackable_dummy_item != global_default_unstackable_dummy_item,
                'unstackable_dummy_item' -> _itemToString(global_unstackable_dummy_item),
            // else
                ...{}
        ),
        ...global_settings
    };
    write_file('config', 'json', data);
);

__on_close() -> (
    saveConfig();
);