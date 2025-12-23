import { useEffect, useState } from 'react';
import './App.css'; // Pode manter ou apagar se quiser limpar o estilo

interface AuditLog {
  id: number;
  eventType: string;
  timestamp: string;
  customerId: number;
  name: string;
  email: string;
}

function App() {
  const [logs, setLogs] = useState<AuditLog[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Note que aqui chamamos a porta 8081 (Audit Service)
    fetch('http://localhost:8081/api/audit')
      .then((res) => res.json())
      .then((data) => {
        setLogs(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Erro de conexão:", err);
        setLoading(false);
      });
  }, []);

  return (
    <div style={{ width: '100%', maxWidth: '1200px', margin: '0 auto', padding: '20px' }}>
      <h1>🕵️ Painel Administrativo de Auditoria</h1>
      
      {loading ? (
        <p>Carregando eventos...</p>
      ) : (
        <table border={1} style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
          <thead>
            <tr style={{ background: '#333', color: 'white' }}>
              <th style={{ padding: '10px' }}>ID</th>
              <th style={{ padding: '10px' }}>Evento</th>
              <th style={{ padding: '10px' }}>Data</th>
              <th style={{ padding: '10px' }}>Cliente</th>
              <th style={{ padding: '10px' }}>Detalhes</th>
            </tr>
          </thead>
          <tbody>
            {logs.length === 0 ? (
               <tr><td colSpan={5} style={{textAlign: 'center', padding: '20px'}}>Nenhum evento encontrado.</td></tr>
            ) : logs.map((log) => (
              <tr key={log.id} style={{ textAlign: 'center' }}>
                <td style={{ padding: '8px' }}>{log.id}</td>
                <td style={{ padding: '8px' }}>
                  <span style={{
                    padding: '5px 10px',
                    borderRadius: '15px',
                    fontSize: '0.8rem',
                    fontWeight: 'bold',
                    backgroundColor: log.eventType === 'CUSTOMER_CREATED' ? '#d4edda' : '#fff3cd',
                    color: '#155724'
                  }}>
                    {log.eventType}
                  </span>
                </td>
                <td style={{ padding: '8px' }}>{new Date(log.timestamp).toLocaleString()}</td>
                <td style={{ padding: '8px' }}>ID: {log.customerId}</td>
                <td style={{ padding: '8px' }}>{log.name} ({log.email})</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default App;