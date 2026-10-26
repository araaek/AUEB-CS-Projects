#pragma once

#include "gameobject.h"
#include <vector>
#include <list>
#include <string>
#include <sgg/graphics.h>
#include "player.h"

class Level : public GameObject
{
	graphics::Brush m_brush_background;

	std::vector<GameObject*> m_static_objects;
	std::list<GameObject*> m_dynamic_objects;

	// add some collidable blocks
	std::vector<Box> m_blocks;
	std::vector<std::string> m_block_names;
	const float m_block_size = 1.0f;
	graphics::Brush m_block_brush;
	graphics::Brush m_block_brush_debug;

	float m_center_x = 5.0f;
	float m_center_y = 5.0f;

	// dedicated method to draw a block
	void drawBlock(int i);

	// detect collisions
	void checkCollisions();
private:
	std::string m_name; // Μεταβλητή για την αποθήκευση του ονόματος του επιπέδου

public:
	void update(float dt) override;
	void draw() override;
	void init() override;

	void addBlock(float x, float y, float w, float h, std::string name);
	Level(const std::string& name = "Level0") : m_name(name) {
		m_brush_background.outline_opacity = 0.0f;
		m_brush_background.texture = m_state->getFullAssetPath("background.png");
	}
	std::string getName() const;

	
	~Level() override;
};