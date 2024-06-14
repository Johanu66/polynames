export class GameService {
    constructor() {  }
  
    static async findByCode(code) {
      const response = await fetch('http://localhost:8080/games/'+code);
      if(response.status === 200)
        {
            const data = await response.json();
            return data;
        }
        return null;
    }

    static async getGame(game_code) {
        const response = await fetch('http://localhost:8080/games/'+game_code);
        if(response.status === 200)
          {
              const data = await response.json();
              return data;
          }
          return null;
    }
  }