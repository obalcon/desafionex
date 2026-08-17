<template>
  <div class="app">
    <nav class="navbar">
      <h1>📦 Controle de Estoque - Nexdom</h1>
      <div class="nav-links">
        <router-link to="/produtos">Produtos</router-link>
        <router-link to="/movimentos">Movimentações</router-link>
        <router-link to="/lucro">Lucro</router-link>
      </div>
    </nav>
    <main>
      <router-view />
    </main>
  </div>
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #f5f5f5;
}

.app {
  min-height: 100vh;
}

.navbar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.navbar h1 {
  font-size: 1.5rem;
}

.nav-links {
  display: flex;
  gap: 1.5rem;
}

.nav-links a {
  color: white;
  text-decoration: none;
  font-weight: 500;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background 0.3s;
}

.nav-links a:hover,
.nav-links a.router-link-active {
  background: rgba(255, 255, 255, 0.2);
}

main {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.btn {
  padding: 0.6rem 1.2rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s;
}

.btn-primary {
  background: #667eea;
  color: white;
}

.btn-primary:hover {
  background: #5a6fd6;
}

.btn-success {
  background: #48bb78;
  color: white;
}

.btn-success:hover {
  background: #38a169;
}

.btn-danger {
  background: #f56565;
  color: white;
}

.btn-danger:hover {
  background: #e53e3e;
}

.btn-warning {
  background: #ed8936;
  color: white;
}

.btn-warning:hover {
  background: #dd6b20;
}

.card {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 1.5rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.3rem;
  font-weight: 500;
  color: #4a5568;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 0.6rem;
  border: 1px solid #e2e8f0;
  border-radius: 4px;
  font-size: 0.9rem;
}

.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #667eea;
}

table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

th,
td {
  padding: 0.8rem;
  text-align: left;
  border-bottom: 1px solid #e2e8f0;
}

th {
  background: #f7fafc;
  font-weight: 600;
  color: #4a5568;
}

tr:hover {
  background: #f7fafc;
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.alert {
  padding: 1rem;
  border-radius: 4px;
  margin-bottom: 1rem;
}

.alert-success {
  background: #c6f6d5;
  color: #22543d;
}

.alert-error {
  background: #fed7d7;
  color: #742a2a;
}

.empty-state {
  text-align: center;
  padding: 3rem;
  color: #718096;
}

.badge {
  padding: 0.2rem 0.6rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
}

.badge-eletronico {
  background: #bee3f8;
  color: #2c5282;
}

.badge-eletrodomestico {
  background: #c6f6d5;
  color: #22543d;
}

.badge-movel {
  background: #feebc8;
  color: #744210;
}

.badge-entrada {
  background: #c6f6d5;
  color: #22543d;
}

.badge-saida {
  background: #fed7d7;
  color: #742a2a;
}
</style>
