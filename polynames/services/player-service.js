export class PlayerService {
  constructor() {  }

  static async create(pseudo) {
    const response = await fetch('http://localhost:8080/players', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({pseudo: pseudo}),
    });
    if(response.status === 200)
      {
          const data = await response.json();
          return data;
      }
      return null;
  }
}