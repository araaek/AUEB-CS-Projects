#include "enemy.h"
#include "util.h"
#include <cmath>
#include <chrono> 


void Enemy::update(float dt)
{
	float delta_time = dt / 1000.0f;

	moveEnemy(dt);
	GameObject::update(dt);
}

void Enemy::draw()
{
	int sprite = (int)fmod(100.0f - m_pos_x * 9.0f, m_sprites.size());
	
	m_brush_enemy.texture = m_sprites[sprite];
	graphics::drawRect(m_pos_x + m_state->m_global_offset_x, m_pos_y + m_state->m_global_offset_y, 1.0f, 1.0f, m_brush_enemy);

	if (m_state->m_debugging)
		debugDraw();
}

void Enemy::init()
{
	srand(time(0));
	m_pos_x = m_state->getCanvasWidth();
	m_pos_y = rand() % int(m_state->getCanvasHeight());
	m_vx = 2.0f; 

	m_brush_enemy.fill_opacity = 1.0f;
	m_brush_enemy.outline_opacity = 0.0f;
	m_brush_enemy.texture = m_state->getFullAssetPath("enemy.png");
	m_sprites.push_back(m_state->getFullAssetPath("enemy.png"));

	// Adjust width for finer collision detection
	m_width = 0.6f;
}

void Enemy::debugDraw()
{
	graphics::Brush debug_brush;
	SETCOLOR(debug_brush.fill_color, 1, 0.3f, 0);
	SETCOLOR(debug_brush.outline_color, 1, 0.1f, 0);
	debug_brush.fill_opacity = 0.1f;
	debug_brush.outline_opacity = 1.0f;
	graphics::drawRect(m_pos_x + m_state->m_global_offset_x, m_pos_y + m_state->m_global_offset_y, m_width, m_height, debug_brush);

	char s[20];
	sprintf_s(s, "(%5.2f, %5.2f)", m_pos_x, m_pos_y);
	SETCOLOR(debug_brush.fill_color, 1, 0, 0);
	graphics::setFont(m_state->getFullAssetPath("OpenSans-Regular.ttf"));
	debug_brush.fill_opacity = 1.0f;
	graphics::drawText(m_pos_x + m_state->m_global_offset_x - 0.4f, m_pos_y + m_state->m_global_offset_y - 0.6f, 0.15f, s, debug_brush);
}

void Enemy::moveEnemy(float dt)
{
	float delta_time = dt / 1000.0f;

	// Ενημερώνουμε μόνο την οριζόντια θέση
	m_pos_x -= m_vx * delta_time;
	float move = 0.0f;
}

