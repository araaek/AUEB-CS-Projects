#include "level.h"
#include <sgg/graphics.h>
#include "player.h"
#include "enemy.h"
#include "util.h"


void Level::drawBlock(int i)
{
	Box& box = m_blocks[i];
	std::string& name = m_block_names[i];

	float x = box.m_pos_x + m_state->m_global_offset_x;
	float y = box.m_pos_y + m_state->m_global_offset_y;

	m_block_brush.texture = m_state->getFullAssetPath(name);

	// graphics::drawRect(x, y, 1.8f * m_block_size, 1.8f * m_block_size, m_block_brush);
	graphics::drawRect(x, y, m_block_size, m_block_size, m_block_brush);

	if (m_state->m_debugging)
		graphics::drawRect(x, y, m_block_size, m_block_size, m_block_brush_debug);
}

void Level::checkCollisions()
{
	for (auto& block : m_blocks)
	{
		float offset = 0.0f;
		if (offset = m_state->getPlayer()->intersectSideways(block))
		{
			m_state->getPlayer()->m_pos_x += offset;

			m_state->getPlayer()->m_vx = 0.0f;
			break;
		}
	}

	for (auto& block : m_blocks)
	{
		float offset = 0.0f;
		if (offset = m_state->getPlayer()->intersectDown(block))
		{
			m_state->getPlayer()->m_pos_y += offset;

			// add sound event
			if (m_state->getPlayer()->m_vy > 1.0f)
				graphics::playSound(m_state->getFullAssetPath("ice1.wav"), 1.0f);

			m_state->getPlayer()->m_vy = 0.0f;
			break;
		}
	}

	for (auto& block : m_blocks)
	{
		float offset = 0.0f;
		if (offset = m_state->getPlayer()->intersectUp(block))
		{
			m_state->getPlayer()->m_pos_y -= offset;

			// add sound event
			if (m_state->getPlayer()->m_vy < -1.0f)
				graphics::playSound(m_state->getFullAssetPath("ice1.wav"), 1.0f);

			m_state->getPlayer()->m_vy = 0.0f;

			break;
		}
	}
}

void Level::update(float dt)
{
	if (m_state->getPlayer()->isActive())
		m_state->getPlayer()->update(dt);

	if (m_state->getEnemy()->isActive())
		m_state->getEnemy()->update(dt);

	checkCollisions();

	GameObject::update(dt);
}

void Level::draw()
{
	float w = m_state->getCanvasWidth();
	float h = m_state->getCanvasHeight();

	float offset_x = m_state->m_global_offset_x / 2.0f + w / 2.0f;
	float offset_y = m_state->m_global_offset_y / 2.0f + h / 2.0f;

	if (m_name == "GameOverLevel") { // αν το παιχνίδι έχει τελειώσει ...
		// Εμφάνιση του σκορ του παίκτη
		std::string scoreText = "Final Score: " + std::to_string(m_state->getPlayer()->getScore());
		graphics::drawText(m_state->getCanvasWidth() * 0.5f - 1.0, m_state->getCanvasHeight() * 0.5f - 0.7f, 0.3, scoreText, m_brush_background);
		graphics::drawText(m_state->getCanvasWidth() * 0.5f - 1.0f, m_state->getCanvasHeight() * 0.5f + 0.7f, 0.2f, "Press escape to exit...", m_brush_background);
	}
	else {

		//draw background
		graphics::drawRect(offset_x, offset_y, 2.0f * w, 2.0f * h, m_brush_background);

		// draw player
		if (m_state->getPlayer()->isActive())
			m_state->getPlayer()->draw();

		// draw blocks
		for (int i = 0; i < m_blocks.size(); i++)
		{
			drawBlock(i);
		}

		// draw enemy
		if (m_state->getEnemy()->isActive())
			m_state->getEnemy()->draw();
	}
}


// adding blocks on stage
void Level::addBlock(float x, float y, float w, float h, std::string name)
{
	m_blocks.push_back(Box(x, y, w, h));
	m_block_names.push_back(name);
}

std::string Level::getName() const
{
	return  m_name;;
}

void Level::init()
{
	// Stage 1
	for (auto p_gob : m_static_objects)
		if (p_gob) p_gob->init();

	for (auto p_gob : m_dynamic_objects)
		if (p_gob) p_gob->init();

	// initialize some collidable blocks here.
	addBlock(1.0f, 1.0f, 2.0f, 2.0, "block7.png");
	addBlock(2.0f, 6.0f, 1.0f, 1.0f, "block6.png");
	addBlock(3.0f, 5.0f, 1.0f, 1.0f, "block6.png");
	addBlock(4.0f, 2.0f, 1.0f, 1.0f, "block2.png");
	addBlock(4.0f, 5.0f, 1.0f, 1.0f, "block1.png");
	addBlock(5.0f, 5.0f, 1.0f, 1.0, "block3.png");
	addBlock(6.0f, 4.0f, 1.0f, 1.0f, "block3.png");
	addBlock(7.0f, 3.0f, 1.0f, 1.0, "block3.png");
	addBlock(7.0f, 6.0f, 1.0f, 1.0f, "block6.png");
	

	// floor
	for (int i = 0; i <= 8; i++)
		addBlock(float(i), 8.0f, 1.0f, 1.0f, "block5.png");

	// roof
	for (int i = 0; i <= 8; i++)
		addBlock(float(i), 0.0f, 1.0f, 1.0f, "block5.png");

	// left
	for (int i = 1; i <= 7; i++)
		addBlock(0.0f, float(i), 1.0f, 1.0f, "block4.png");

	// right
	for (int i = 1; i <= 7; i++)
		addBlock(8.0f, float(i), 1.0f, 1.0f, "block4.png");

	m_block_brush.outline_opacity = 0.0f;
	m_block_brush_debug.fill_opacity = 0.1f;
	SETCOLOR(m_block_brush_debug.fill_color, 0.1f, 1.0f, 0.1f);
	SETCOLOR(m_block_brush_debug.outline_color, 0.3f, 1.0f, 0.2f);
}

Level::~Level()
{
	for (auto p_go : m_static_objects)
		delete p_go;
	for (auto p_go : m_dynamic_objects)
		delete p_go;
}
