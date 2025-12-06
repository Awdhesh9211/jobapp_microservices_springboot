import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from "path"

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  server: {
    proxy: {
      '/companies': 'http://localhost:8081',
      '/jobs': 'http://localhost:8082',
      '/reviews': 'http://localhost:8083'
    }
  }
})
