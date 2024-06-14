export class TurnService {
    constructor() {  }

    static async update(turn) {
        const response = await fetch('http://localhost:8080/turns/' + turn.id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(turn),
        });
        if(response.status === 200)
        {
            return true;
        }
        return false;
    }
  }