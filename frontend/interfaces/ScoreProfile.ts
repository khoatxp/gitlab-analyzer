export default interface ScoreProfile{
    id: number,
    name: string,
    lineWeight: number,
    deleteWeight: number,
    syntaxWeight: number,
    commentsWeight: number,
    extension: Map<string,number>,
}