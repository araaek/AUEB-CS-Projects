#pragma once

#include "gameobject.h"
#include <sgg/graphics.h>
#include "box.h"

class Enemy : public Box, public GameObject
{
	// animated enemy
	std::vector<std::string> m_sprites;

	graphics::Brush m_brush_enemy;

public:
	float m_vx = 0.0f;
	float m_vy = 0.0f;

public:
	void update(float dt) override;
	void draw() override;
	void init() override;
	Enemy(std::string name) : GameObject(name) {};

protected:
	void debugDraw();

	// dynamic motion control
	void moveEnemy(float dt);
};


