#[test]
fn simple_playground_work() {
    let mut playground = super::Playground::new(2, 2);
    playground.fill(vec!["# ".to_string(), "#w".to_string()]);
    assert_eq!(super::Filler::on(&playground, "\t"),
        "\tlet playground = Playground::new(2, 2);\n\tplayground.fill(vec![\n\t\t\"# \",\n\t\t\"#w\"\n\t\t]);\n");
}
