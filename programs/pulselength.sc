// PulseLength by CommandLeo

global_color = '#E74C3C';
global_valid_blockstates = {'powered', 'power', 'extended', 'triggered', 'enabled'};
global_littables = {'redstone_torch', 'redstone_wall_torch', 'redstone_lamp', 'redstone_ore'};
global_highlight_color = 0xE74C3C88;

global_monitored = {};

_checkVersion(version) -> (
    regex = '(\\d+)\.(\\d+)\.(\\d+)';
    target_version = map(version~regex, number(_));
    scarpet_version = map(system_info('scarpet_version')~regex, number(_));
    return(scarpet_version >= target_version);
);

__config() -> {
    'commands' -> {
        '' -> 'help',
        'monitor' -> ['monitor', null],
        'monitor <position>' -> 'monitor',
        'unmonitor' -> ['unmonitor', null],
        'unmonitor <position>' -> 'unmonitor',
        'clear' -> 'clear',
        'list' -> 'list',
        'highlight' -> 'highlightAll',
        'highlight <position> <dimension>' -> 'highlight',
    },
    'arguments' -> {
        'position' -> {
            'type' -> 'pos',
            'loaded' -> true
        }
    },
    'scope' -> 'player'
};

_error(error) -> (
    print(format(str('r %s', error)));
    run(str('playsound block.note_block.didgeridoo master %s', player()));
    exit();
);

help() -> (
    texts = [
        'fs ' + ' ' * 80, ' \n',
        '#C0392Bb PulseLength', if(_checkVersion('1.4.57'), '@https://github.com/CommandLeo/scarpet/wiki/PulseLength'), 'g  by ', '#%color%b CommandLeo', '^g https://github.com/CommandLeo', if(_checkVersion('1.4.57'), '@https://github.com/CommandLeo'), ' \n\n',
        '%color% /%app_name% monitor [<position>]', 'f ｜', 'g Starts monitoring the block you are looking at or at the specified position', ' \n',
        '%color% /%app_name% unmonitor [<position>]', 'f ｜', 'g Unmonitors the block you are looking at or at the specified position', ' \n',
        '%color% /%app_name% clear', 'f ｜', 'g Unmonitors all blocks', ' \n',
        '%color% /%app_name% list', 'f ｜', 'g Lists all monitored blocks', ' \n',
        '%color% /%app_name% highlight', 'f ｜', 'g Highlights all monitored blocks', ' \n',
        'fs ' + ' ' * 80
    ];
    replacement_map = {'%app_name%' -> system_info('app_name'), '%color%' -> global_color};
    print(format(map(texts, reduce(pairs(replacement_map), replace(_a, ..._), _))));
);

isValid(block) -> (
    return(
        block~'command_block' != null // isCommandBlock
        || has(global_littables, str(block)) // isLittable
        || first(global_valid_blockstates, block_state(block, _)) != null // isRedstoneComponent
    );
);

isActive(block) -> (
    return(
        bool(block_state(block, 'extended')) // isExtendedPiston
        || bool(block_state(block, 'triggered')) // isTriggered
        || block == 'hopper' && !bool(block_state(block, 'enabled')) // isDisabledHopper
        || block_state(str(block), 'lit') != block_state(block, 'lit') // isLit
        || bool(block_state(block, 'powered')) || block_state(block, 'power') > 0 || bool(block_data(block):'powered') // isPowered
    );
);

monitor(position) -> (
    target = if(position, block(position), query(player(), 'trace', 5, 'blocks'));
    if(!target, _error('You must be looking at a block'));
    if(!isValid(target), _error('Invalid block'));

    dimension = current_dimension();
    data = {'position' -> pos(target), 'dimension' -> dimension};
    if(has(global_monitored, data), _error('That block is already being monitored'));
    global_monitored += data;

    run(str('playsound minecraft:block.note_block.pling master %s', player()));
    print(format('f » ', 'g Started monitoring ', str('gi %s', target), 'g  at ', str('%s %s', global_color, str(pos(target))), str('^g %s', dimension), str('!/%s highlight %d %d %d %s', system_info('app_name'), ...pos(target), dimension)));
);

unmonitor(position) -> (
    target = if(position, block(position), query(player(), 'trace', 5, 'blocks'));
    if(!target, _error('You must be looking at a block'));

    dimension = current_dimension();
    data = {'position' -> pos(target), 'dimension' -> dimension};
    if(!has(global_monitored, data), _error('That block is not being monitored'));
    delete(global_monitored, data);

    run(str('playsound minecraft:item.shield.break master %s', player()));
    print(format('f » ', 'g Successfully unmonitored ', str('gi %s', target), 'g  at ', str('%s %s', global_color, str(pos(target))), str('^g %s', dimension)));
);

clear() -> (
    l = length(global_monitored);
    if(!l, _error('No blocks are being monitored'));

    global_monitored = {};
    print(format('f » ', 'g Unmonitored ', str('%s %s ', global_color, l), str('g block%s', if(l == 1, '', 's'))));
);

list() -> (
    l = length(global_monitored);
    if(!l, _error('No blocks are being monitored'));
    run(str('playsound minecraft:block.note_block.pling master %s', player()));
    texts = reduce(global_monitored, dimension = _:'dimension';
        position = _:'position';
        block = in_dimension(dimension, block(position));
        [..._a, ' \n', str('%s %s', global_color, str(position)), str('^g %s', dimension), str('!/execute in %s run tp @s %d %d %d', dimension, ...position), str('g  %s ', block), '#EB4D4Bb ❌', '^g Click to unmonitor', str('!/execute in %s run %s unmonitor %d %d %d', dimension, system_info('app_name'), ...position), 'f ｜', '#8E44ADb ⚜', '^g Click to teleport', str('!/execute in %s run tp @s %d %d %d', dimension, ...position), 'f ｜', '#FBC531b ☀', '^g Click to highlight', str('!/%s highlight %d %d %d %s', system_info('app_name'), ...position, dimension)],
        ['f » ', 'g Monitoring ', str('%s %s ', global_color, l), str('g block%s:', if(l == 1, '', 's'))]
    );
    print(format(texts));
);

highlightAll() -> (
    l = length(global_monitored);
    if(l < 1, _error('There are no blocks to highlight'));

    for(global_monitored, in_dimension(_:'dimension', draw_shape('box', 100, {'player' -> player(), 'from' -> _:'position', 'to' -> _:'position' + 1, 'fill' -> global_highlight_color, 'color' -> 0})));
    
    run(str('playsound minecraft:entity.evoker.cast_spell master %s', player()));
    print(format('f » ', 'g Highlighted ', str('%s %s ', global_color, l), str('g blocks%s', if(l == 1, '', 's'))));
);

highlight(position, dimension) -> (
    if(!has(global_monitored, {'position' -> position, 'dimension' -> dimension}), _error('That block is not being monitored'));

    in_dimension(dimension, draw_shape('box', 100, {'player' -> player(), 'from' -> position, 'to' -> position + 1, 'fill' -> global_highlight_color, 'color' -> 0}));
    run(str('playsound minecraft:entity.evoker.cast_spell master %s', player()));
);

pulselength() -> (
    for(global_monitored,
        dimension = _:'dimension';
        position = _:'position';
        block = in_dimension(dimension, block(position));
        if(!(isValid(block) || block == 'moving_piston'), delete(global_monitored, _),
            isActive(block), global_monitored:_ += 1,
            global_monitored:_ > 0, (
                if(player(), print(player(), format('#C0392Bb PulseLength', 'f  » ', str('gi %s', block), 'g  at ', str('%s %s', global_color, str(position)), str('^g %s', dimension), str('!/%s highlight %d %d %d %s', system_info('app_name'), ...position, dimension), 'f  » ', str('%s %dgt', global_color, global_monitored:_))));
                global_monitored:_ = 0
            )
        );
    );
    schedule(1, 'pulselength');
);

__on_start() -> pulselength();