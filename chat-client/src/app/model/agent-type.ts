import { Host } from "./host";

export class AgentType {
    constructor(
        public name: string,
        public host: Host
    ) {}
}