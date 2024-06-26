export class PlayerService {
  constructor() {  }

  static async create(player) {
    const response = await fetch('http://localhost:8080/players', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(player),
    });
    if(response.status === 200)
      {
          const data = await response.json();
          return data;
      }
      return null;
  }

  static async updatePlayer(player) {
    const response = await fetch('http://localhost:8080/players/'+player.id, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(player),
    });
  }
}