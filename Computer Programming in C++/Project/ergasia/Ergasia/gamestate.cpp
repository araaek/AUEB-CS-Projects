#include "gamestate.h"
#include "level.h"
#include "player.h"
#include "enemy.h"
#include <thread>
#include <chrono>
using namespace std::chrono_literals;

GameState::GameState()
{
}

void GameState::startBackgroundMusic()
{
	// Για την αναπαραγωγή μουσικής όση ώρα παίζεται το παιχνίδι
	std::string musicPath = getFullAssetPath("wintersong.mp3"); 
	graphics::playMusic(musicPath, 0.5f, true); // Αναπαραγωγή μουσικής σε loop με ένταση 50%
}

GameState::~GameState()
{
	if (m_current_level)
		delete m_current_level;
}

GameState* GameState::getInstance()
{
	if (!m_unique_instance)
	{
		m_unique_instance = new GameState();
	}
	return m_unique_instance;
}

bool GameState::init()
{
	m_current_level = new Level("1.lvl");
	m_current_level->init();

	m_player = new Player("Player");
	m_player->init();

	m_enemy = new Enemy("Enemy");
	m_enemy->init();

	graphics::preloadBitmaps(getAssetDir());
	graphics::setFont(m_asset_path + "OpenSans-Regular.ttf");
	startBackgroundMusic();

	return true;
}

void GameState::draw()

{
	if (!m_current_level)
		return;

	m_current_level->draw();

	// Ελέγουμε αν το τρέχον επίπεδο ΔΕΝ είναι το GameOverLevel πριν σχεδιάσουμε τους εχθρούς
	if (m_current_level->getName() != "GameOverLevel") {
		// Σχεδίαση κάθε Enemy
		for (Enemy* enemy : m_enemies) {
			enemy->draw();
		}
	}
}

void GameState::update(float dt)
{
	if (m_player->getHealth() <= 0) { // Αν το health είναι μηδέν
		// Διαγράφουμε το παλιό επίπεδο
		delete m_current_level;

		// Δημιουργούμε ένα νέο επίπεδο
		m_current_level = new Level("GameOverLevel");
		m_current_level->init();

		// Σταματάμε την μουσική
		graphics::stopMusic();
		return;
	}
	else {
		// Δημιουργία ενός νέου Enemy τυχαία
		static float enemySpawnTimer = 0.0f;
		enemySpawnTimer += dt;
		if (enemySpawnTimer > 1000) { // Δημιουργία ενός νέου Enemy κάθε 1 δευτερόλεπτo
			Enemy* newEnemy = new Enemy("Enemy");
			newEnemy->init(); 
			m_enemies.push_back(newEnemy);
			enemySpawnTimer = 0.0f;
		}

		// Ενημέρωση κάθε Enemy και έλεγχος για συγκρούσεις
		auto it = m_enemies.begin();
		while (it != m_enemies.end()) {
			Enemy* enemy = *it;
			enemy->update(dt);

			// Ελέγχος για σύγκρουση μεταξύ του Player και του τρέχοντος Enemy
			if (m_player->intersect(*enemy)) {
				// Σε περίπτωση σύγκρουσης, καταστρέφεται ο Enemy
				graphics::playSound(getFullAssetPath("boom.mp3"), 1.0f);
				delete enemy;
				m_player->m_health -= 25;
				if (m_player->m_health <= 0)
					m_player->m_health = 0;
				it = m_enemies.erase(it);
			}
			// Ελέγχος  αν ο Enemy έχει βγει εκτός καμβά
			else if (enemy->m_pos_x < 0) {
				delete enemy;
				it = m_enemies.erase(it);
			}
			else {
				++it; // Μετακίνηση στον επόμενο Enemy, αν δεν υπάρχει σύγκρουση
			}
		}
	}

	// Skip an update if a long delay is detected 
	// to avoid messing up the collision simulation
	if (dt > 500) // ms
		return;

	// Avoid too quick updates
	float sleep_time = std::max(17.0f - dt, 0.0f);
	if (sleep_time > 0.0f)
	{
		std::this_thread::sleep_for(std::chrono::duration<float, std::milli>(sleep_time));
	}

	if (!m_current_level)
		return;

	m_current_level->update(dt);


	m_debugging = graphics::getKeyState(graphics::SCANCODE_0);


}

std::string GameState::getFullAssetPath(const std::string& asset)
{
	return m_asset_path + asset;
}

std::string GameState::getAssetDir()
{
	return m_asset_path;
}

GameState* GameState::m_unique_instance = nullptr;