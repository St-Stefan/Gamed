### Team 65

# Gamed
A social game curation platform.

## Description
Gamed is a curated, community-driven platform specifically designed for video gamers to enhance discovery and engagement. This platform offers several features:

- Granular Game Ratings: Users can rate games across multiple metrics (e.g., graphics, music, gameplay enjoyment) through a nuanced rating system that tracks player enjoyment across different parts or acts of a game. This adds depth to traditional rating systems and provides more detailed feedback.
- Personalized Recommendations: By curating game lists and analyzing shared preferences, the platform can generate personalized game recommendations, similar to how Spotify creates music playlists. This allows players to discover new games based on others with similar tastes.
- Social Interaction and Community Engagement: Gamed encourages users to curate and share their favorite game lists, compare preferences, and engage with others. This fosters a deeper sense of community and personalized discovery through social interaction.

Gamed is designed to provide a personalized and social gaming experience. Users can discover new games based on similar player preferences, offer detailed feedback on different aspects of a game, and creatively share their gaming tastes in a meaningful way.

## Visuals

NA

## Installation
1. Clone the project via Gitlab:

    `git clone git@gitlab.ewi.tudelft.nl:cs4505/2024-2025/teams/team-65.git`

2. Or download the source files from the available releases

## Usage
This project requires docker to be installed. You can do so via the command line:

1. Debian/Ubuntu
     
    `sudo apt-get install docker`
2. MacOS
    
    `brew install docker`

Currently the project only contains placeholder spring boot services and dockerized database microservices. You can run any dockerized database via:

    mvn clean package
    docker compose up

Remember to close the images by using: 

    docker compose down


## Roadmap
We expect the next versions to contain:

- [ ] WebUI for browsing the home page
- [ ] WebUI for checking your profile
- [ ] Microservice for Reviewing Games
- [ ] Microservice for User Page Information
- [ ] Microservice for Timeline Information
- [ ] Microservice for Searching Lists or Users

## Contributing
We are open to any suggestions you might have on the functionality or code of Gamed, however at this time we will not be accepting any third-party commits on our repo.

## Authors and acknowledgment

- Andrei Ionescu
- Konrad Barbers
- Matei Ivan
- Stefan Stoicescu

## License
For open source projects, say how it is licensed.

## Project status
Currently active.
