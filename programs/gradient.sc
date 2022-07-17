global_charset = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 'A', 'B', 'C', 'D', 'E', 'F'];

__config() -> {
    'commands' -> {
        '' -> 'help',
        'print <text> <start_color> <end_color>' -> ['printGradient', false],
        'print <text> <start_color> <end_color> bold' -> ['printGradient', true],
        'team <text> <start_color> <end_color>' -> ['teamGradient', false],
        'team <text> <start_color> <end_color> bold' -> ['teamGradient', true]
    },
    'arguments' -> {
        'text' -> {
            'type' -> 'string',
            'suggest' -> []
        },
        'start_color' -> {
            'type' -> 'string',
            'suggester' -> _(args) -> (
                color = upper(args:'start_color' || '');
                if(!color || (length(color) < 6 && all(split(color), global_charset~_ != null)), map(global_charset, color + _));
            ),
            'case_sensitive' -> false
        },
        'end_color' -> {
            'type' -> 'string',
            'suggester' -> _(args) -> (
                color = upper(args:'end_color' || '');
                if(!color || (length(color) < 6 && all(split(color), global_charset~_ != null)), map(global_charset, color + _));
            ),
            'case_sensitive' -> false
        }
    }
};

// HELPER FUNCTIONS

_error(error) -> exit(print(format(str('r %s', error))));

validateHex(string) -> (
    hex = upper(string)~'^#?([0-9A-F]{6}|[0-9A-F]{3})$';
    if(length(hex) == 3, hex = replace(hex, '(.)', '$1$1'));
    return(hex);
);

rgbToHex(rgb) -> upper(str('#%02x%02x%02x', rgb));

hexToRgb(hex) -> (
    values = map(split(hex), global_charset~_);
    r = values:0 * 16 + values:1;
    g = values:2 * 16 + values:3;
    b = values:4 * 16 + values:5;
    return([r, g, b]);
);

generateGradient(text, start_color, end_color, bold) -> (
    input = split(text);
    l = length(filter(input, _ != ' ')) - 1;

    start_color = validateHex(start_color);
    end_color = validateHex(end_color);
    if(!start_color, _error('The start color is not a valid hex color'));
    if(!end_color, _error('The end color is not a valid hex color'));

    start_rgb = hexToRgb(start_color);    
    end_rgb = hexToRgb(end_color);
    increment = (end_rgb - start_rgb) / l;

    i = -1;
    output = map(input, if(_ == ' ', ' \ ', str('%s%s %s', if(bold, 'b', ''), rgbToHex(start_rgb + increment * (i += 1)), _)));
    return(output);
);

// MAIN

help() -> (
    texts = [
        'fs ' + ' ' * 80, ' \n',
        ...generateGradient('Gradient Generator', '#6AB04C', '#22A6B3', true), 'g  by ', '#1ABC9Cb CommandLeo', '^g https://github.com/CommandLeo', ' \n\n',
        '#1ABC9C /app_name print <text> <start_color> <end_color> [bold] ', 'f ｜ ', 'g Prints to chat a [bold] text colored with a gradient from <start_color> to <end_color>', ' \n',
        '#1ABC9C /app_name team <text> <start_color> <end_color> [bold] ', 'f ｜ ', 'g Sets as the prefix of the team you belong to a [bold] text colored with a gradient from <start_color> to <end_color>', ' \n',
        'fs ' + ' ' * 80
    ];
    print(format(map(texts, replace(_, 'app_name', system_info('app_name')))));
);

printGradient(text, start_color, end_color, bold) -> (
    gradient = generateGradient(text, start_color, end_color, bold);
    print(format(gradient));
);

teamGradient(text, start_color, end_color, bold) -> (
    team = player()~'team';
    if(!team, _error('You must be in a team!'));
    team_property(team, 'prefix', format(generateGradient(text, start_color, end_color, bold)) + ' ');
);