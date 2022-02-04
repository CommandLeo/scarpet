// PulseLength by CommandLeo

global_app_name = system_info('app_name');
global_monitored = {};
global_valid_blockstates = {'powered', 'power', 'extended', 'triggered', 'enabled'};
global_littables = {'redstone_torch', 'redstone_wall_torch', 'redstone_lamp', 'redstone_ore'};
global_highlight_color = 3880533128;

__config() -> {
    'commands' -> {
        '' -> 'help',
        'monitor' -> ['monitor', null],
        'monitor <pos>' -> 'monitor',
        'unmonitor' -> ['unmonitor', null],
        'unmonitor <pos>' -> 'unmonitor',
        'clear' -> 'clear',
        'list' -> 'list',
        'highlight' -> 'highlight',
        'highlight <pos> <dimension>' -> 'highlightOne',
    },
    'scope' -> 'player'
};

_error(msg) -> (
    run(str('playsound minecraft:block.note_block.didgeridoo master %s', player()));
    print(format('r ' + msg));
    exit();
);

help() -> (
    texts = [
        'fs -----------------------------------------------------', ' \n',
        '#C0392Bb PulseLength ', 'g by ', '#E74C3Cb CommandLeo', ' \n\n',
        '#E74C3C /app_name monitor [<position>] ', 'f ｜ ', 'g Starts monitoring the block you are looking at or the specified position', ' \n',
        '#E74C3C /app_name unmonitor [<position>] ', 'f ｜ ', 'g Unmonitors the block you are looking at or the specified position', ' \n',
        '#E74C3C /app_name clear ', 'f ｜ ', 'g Unmonitors all blocks', ' \n',
        '#E74C3C /app_name list ', 'f ｜ ', 'g Lists all monitored blocks', ' \n',
        '#E74C3C /app_name highlight ', 'f ｜ ', 'g Highlights all monitored blocks', ' \n',
        'fs -----------------------------------------------------'
    ];
    print(format(map(texts, replace(_, 'app_name', global_app_name))));
);

isValid(block) -> (
    bool(
        isCommandBlock = block~'command_block' // isCommandBlock
        || first(global_valid_blockstates, block_state(block, _)) // isRedstoneComponent
        || has(global_littables, str(block)) // isLittable
    );
);

isActive(block) -> (
    bool(
        bool(block_state(block, 'extended')) // isExtendedPiston
        || bool(block_state(block, 'triggered')) // isTriggered
        || block == 'hopper' && !bool(block_state(block, 'enabled')) // isDisabledHopper
        || block_state(str(block), 'lit') != block_state(block, 'lit') // isLit
        || bool(block_state(block, 'powered')) || block_state(block, 'power') > 0 || block_data(block):'powered' // isPowered
    );
);

monitor(position) -> (
    b = position || player()~'trace';
    if(!b || type(b) == 'entity' || !isValid(block(b)), _error('Invalid block!'));
    b = block(b);
    dimension = current_dimension();
    data = {'position' -> pos(b), 'dimension' -> dimension};
    if(has(global_monitored, data), _error('That block is already being monitored!'));
    global_monitored += data;
    run(str('playsound minecraft:block.note_block.pling master %s', player()));
    print(format('f » ', str('g Started monitoring %s at ', b), '#E74C3C ' + pos(b), '^g Click to highlight', str('!/%s highlight %d %d %d %s', global_app_name, ...pos(b), dimension), str('g  in %s', dimension)));
);

unmonitor(position) -> (
    b = position || player()~'trace';
    if(!b || type(b) == 'entity' || !isValid(block(b)), _error('Invalid block!'));
    b = block(b);
    dimension = current_dimension();
    data = {'position' -> pos(b), 'dimension' -> dimension};
    if(!has(global_monitored, data), _error('That block is not being monitored!'));
    delete(global_monitored, data);
    run(str('playsound minecraft:block.anvil.place master %s', player()));
    print(format('f » ', str('g Successfully unmonitored %s at ', b), '#E74C3C ' + pos(b), str('g  in %s', dimension)));
);

clear() -> (
    l = length(global_monitored);
    if(l < 1, _error('No blocks are being monitored'));
    global_monitored = {};
    print(format('f » ', 'g Unmonitored ', str('#E74C3C %s ', l), str('g block%s', if(l == 1, '', 's'))));
);

list() -> (
    l = length(global_monitored);
    if(l < 1, _error('No blocks are being monitored'));
    run(str('playsound minecraft:block.note_block.pling master %s', player()));
    print(format('f » ', 'g Monitoring ', str('#E74C3C %s ', l), str('g position%s:', if(l == 1, '', 's'))));
    for(global_monitored, (
        dimension = _:'dimension';
        position = _:'position';
        block = in_dimension(dimension, block(position));
        print(format('#E74C3C  ' + position , str('^g %s', dimension), str('!/execute in %s run tp @s %d %d %d', dimension, ...position), str('g  %s ', block), '#EB4D4Bb ❌', '^g Click to unmonitor', str('!/execute in %s run %s unmonitor %d %d %d', dimension, global_app_name, ...position), 'f  ｜ ', '#8E44ADb ⚜', '^g Click to teleport', str('!/execute in %s run tp @s %d %d %d', dimension, ...position), 'f  ｜ ', '#FBC531b ☀', '^g Click to highlight', str('!/%s highlight %d %d %d %s', global_app_name, ...position, dimension)));
    ));
);

highlight() -> (
    l = length(global_monitored);
    if(l < 1, _error('There are no blocks to highlight'));
    for(global_monitored, in_dimension(_:'dimension', draw_shape('box', 100, {'player' -> player(), 'from' -> _:'position', 'to' -> _:'position' + 1, 'fill' -> global_highlight_color, 'color' -> 0})));
    run(str('playsound minecraft:entity.evoker.cast_spell master %s', player()));
    print(format('f » ', 'g Highlighted ', str('#E74C3C %s ', l), str('g blocks%s', if(l == 1, '', 's'))));
);

highlightOne(position, dimension) -> (
    if(!has(global_monitored, {'position' -> position, 'dimension' -> dimension}), _error('That block is not being monitored!'));
    draw_shape('box', 100, {'player' -> player(), 'from' -> position, 'to' -> position + 1, 'fill' -> global_highlight_color, 'color' -> 0});
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
                print(format('#C0392Bb PulseLength', 'f  » ', str('g %s at ', block), '#E74C3C ' + position, '^g Click to highlight', str('!/%s highlight %d %d %d %s', global_app_name, ...position, dimension), 'f  » ', str('#E74C3C %dgt', global_monitored:_)));
                global_monitored:_ = 0
            )
        );
    );
    schedule(1, 'pulselength');
);

__on_start() -> pulselength();
