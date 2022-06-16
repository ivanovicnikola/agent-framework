import { AgentType } from "./agent-type";
import { Host } from "./host";

export class AID {
    constructor(
        public name: string,
        public host: Host,
        public type: AgentType
    ) {}
}