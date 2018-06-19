use spectral::prelude::*;

use Playground;
use Game;
use Round;
use Entity;
use Static;
use EntityType::WANDERER;
use EntityType::EXPLORER;

#[test]
fn simple_playground_work() {
    let mut playground = Playground::new(2, 2, Static::Empty);
    playground.fill(vec!["# ".to_string(), "#w".to_string()]);
    assert_eq!(super::Filler::on(&playground, "\t"),
        "\tlet mut playground = Playground::new(2, 2, Static::Empty);\n\tplayground.fill(vec![\n\t\t\"# \".to_string(),\n\t\t\"#w\".to_string()\n\t\t]);\n");
}

#[test]
fn just_go_away_when_wanderer_come() {
	let mut playground = Playground::new(20, 16, Static::Empty);
	playground.fill(vec![
		"####################".to_string(),
		"##                ##".to_string(),
		"# w### ###### ###w #".to_string(),
		"# #    #    #    # #".to_string(),
		"# # ## # ## # ## # #".to_string(),
		"#   ##   ##   ##   #".to_string(),
		"# #    #    #    # #".to_string(),
		"# ### ## ## ## ### #".to_string(),
		"# ### ## ## ## ### #".to_string(),
		"# #    #    #    # #".to_string(),
		"#   ##   ##   ##   #".to_string(),
		"# # ## # ## # ## # #".to_string(),
		"# #    #    #    # #".to_string(),
		"# w### ###### ###w #".to_string(),
		"##                ##".to_string(),
		"####################".to_string()
		]);
	let game = Game::new(playground, 3, 1, 3, 40);
	let entities = vec![
		Entity { entity_type:EXPLORER, id:0, x:8, y:7, param0:139, param1:2, param2:3 },
		Entity { entity_type:EXPLORER, id:1, x:11, y:7, param0:199, param1:2, param2:3 },
		Entity { entity_type:EXPLORER, id:2, x:8, y:7, param0:159, param1:2, param2:3 },
		Entity { entity_type:EXPLORER, id:3, x:11, y:8, param0:199, param1:2, param2:3 },
		Entity { entity_type:WANDERER, id:10, x:8, y:8, param0:27, param1:1, param2:0 },
		Entity { entity_type:WANDERER, id:11, x:6, y:11, param0:32, param1:1, param2:0 },
		Entity { entity_type:WANDERER, id:12, x:4, y:14, param0:37, param1:1, param2:0 },
		Entity { entity_type:WANDERER, id:13, x:2, y:13, param0:2, param1:0, param2:-1 }
		];
	let round = Round::new(game, entities);
    assert_that!(&round.compute()).starts_with("MOVE 8 6");
}
