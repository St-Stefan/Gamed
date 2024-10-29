const UserDatabaseAPI = "http://localhost:8090"

export default {
    async createUser(userData){
        try{
            let response = await fetch(`${UserDatabaseAPI}/users/create`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userData),
            });
            if(!response.ok){
                throw new Error("Could not create user!");
            }
            return await response.text();
        }
        catch (error){
            throw error;
        }
    }
}