import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    watch: {
      usePolling: true, // Essencial para o WSL atualizar ao salvar
    },
    host: true, 
    strictPort: true,
    port: 5173, // A porta INTERNA do container continua a padrão
  }
})