// Bot Team by CommandLeo

global_team_name = 'bots';

__on_start() -> (
    if(team_list()~global_team_name == null,
        team_add(global_team_name);
        team_property(global_team_name, 'color', 'gray');
        team_property(global_team_name, 'prefix', '[BOT] ');
    );
);

__on_player_connects(player) -> (
    data = load_app_data() || {};
    team = player~'team';
    
    if(player~'player_type' == 'fake',
        if(team,
            if(team != global_team_name, data:str(player) = team),
            delete(data, str(player))
        );
        store_app_data(data);
        team_add(global_team_name, player),
    team == global_team_name,
        previous_team = data:str(player);
        if(previous_team && team_list()~previous_team != null,
            team_add(previous_team, player),
            team_leave(player)
        );
    );
);