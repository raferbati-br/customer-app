import { useEffect, useState } from "react";
import {
  AppBar,
  Box,
  Button,
  Container,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
  Toolbar,
  Typography,
} from "@mui/material";
import { Delete, Edit } from "@mui/icons-material";

interface Customer {
  id?: number;
  name: string;
  email: string;
  document: string;
  phone?: string;
}

const API_BASE = "http://localhost:8080/api/customers";

const emptyCustomer: Customer = {
  name: "",
  email: "",
  document: "",
  phone: "",
};

function App() {
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [form, setForm] = useState<Customer>(emptyCustomer);
  const [loading, setLoading] = useState(false);

  async function loadCustomers() {
    setLoading(true);
    const res = await fetch(API_BASE);
    if (!res.ok) {
      console.error("Erro ao carregar clientes", res.status);
      setLoading(false);
      return;
    }
    const data = await res.json();
    setCustomers(data);
    setLoading(false);
  }

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    loadCustomers();
  }, []);

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();

    const method = form.id ? "PUT" : "POST";
    const url = form.id ? `${API_BASE}/${form.id}` : API_BASE;

    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(form),
    });

    if (!res.ok) {
      alert("Erro ao salvar cliente");
      return;
    }

    setForm(emptyCustomer);
    loadCustomers();
  }

  function handleEdit(customer: Customer) {
    setForm(customer);
  }

  async function handleDelete(id?: number) {
    if (!id) return;
    if (!confirm("Confirmar exclusão?")) return;

    const res = await fetch(`${API_BASE}/${id}`, { method: "DELETE" });
    if (!res.ok) {
      alert("Erro ao excluir cliente");
      return;
    }
    loadCustomers();
  }

  function handleCancel() {
    setForm(emptyCustomer);
  }

  return (
    <Box sx={{ flexGrow: 1 }}>
      {/* AppBar */}
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div">
            Cadastro de Clientes
          </Typography>
        </Toolbar>
      </AppBar>

      {/* Conteúdo principal */}
      <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
        {/* Formulário */}
        <Paper sx={{ p: 3, mb: 4 }}>
          <Typography variant="h6" gutterBottom>
            {form.id ? "Editar cliente" : "Novo cliente"}
          </Typography>

          <Box
            component="form"
            onSubmit={handleSubmit}
            sx={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 2 }}
          >
            <TextField
              label="Nome"
              name="name"
              value={form.name ?? ""}
              onChange={handleChange}
              required
              fullWidth
            />
            <TextField
              label="Email"
              name="email"
              value={form.email ?? ""}
              onChange={handleChange}
              type="email"
              required
              fullWidth
            />
            <TextField
              label="Documento"
              name="document"
              value={form.document ?? ""}
              onChange={handleChange}
              required
              fullWidth
            />
            <TextField
              label="Telefone"
              name="phone"
              value={form.phone ?? ""}
              onChange={handleChange}
              fullWidth
            />

            <Box sx={{ gridColumn: "1 / -1", mt: 1 }}>
              <Button type="submit" variant="contained">
                {form.id ? "Atualizar" : "Incluir"}
              </Button>
              {form.id && (
                <Button
                  type="button"
                  variant="text"
                  color="inherit"
                  onClick={handleCancel}
                  sx={{ ml: 2 }}
                >
                  Cancelar
                </Button>
              )}
            </Box>
          </Box>
        </Paper>

        {/* Tabela de clientes */}
        <Paper sx={{ p: 2 }}>
          <Typography variant="h6" gutterBottom>
            Lista de clientes
          </Typography>

          {loading ? (
            <Typography>Carregando...</Typography>
          ) : customers.length === 0 ? (
            <Typography>Nenhum cliente cadastrado.</Typography>
          ) : (
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell>ID</TableCell>
                  <TableCell>Nome</TableCell>
                  <TableCell>Email</TableCell>
                  <TableCell>Documento</TableCell>
                  <TableCell>Telefone</TableCell>
                  <TableCell align="right">Ações</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {customers.map(c => (
                  <TableRow key={c.id}>
                    <TableCell>{c.id}</TableCell>
                    <TableCell>{c.name}</TableCell>
                    <TableCell>{c.email}</TableCell>
                    <TableCell>{c.document}</TableCell>
                    <TableCell>{c.phone}</TableCell>
                    <TableCell align="right">
                      <IconButton
                        aria-label="editar"
                        size="small"
                        onClick={() => handleEdit(c)}
                      >
                        <Edit fontSize="small" />
                      </IconButton>
                      <IconButton
                        aria-label="excluir"
                        size="small"
                        onClick={() => handleDelete(c.id)}
                      >
                        <Delete fontSize="small" />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </Paper>
      </Container>
    </Box>
  );
}

export default App;
