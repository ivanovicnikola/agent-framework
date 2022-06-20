import { AgentType } from "./agent-type";
import { Host } from "./host";

export class AID {
    constructor(
        public name: string,
        public type: AgentType
    ) {}
}