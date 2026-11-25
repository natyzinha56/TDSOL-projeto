document.addEventListener('DOMContentLoaded', () => {
  const selectTipo = document.getElementById('tipo');
  const camposDoador = document.getElementById('camposDoador');
  const camposInstituicao = document.getElementById('camposInstituicao');
  const formCadastro = document.getElementById('formCadastro');
  const mensagem = document.getElementById('mensagemCadastro');

  function atualizarCamposPorTipo() {
    if (!selectTipo) return;
    if (selectTipo.value === 'DOADOR') {
      camposDoador.style.display = 'block';
      camposInstituicao.style.display = 'none';
    } else {
      camposDoador.style.display = 'none';
      camposInstituicao.style.display = 'block';
    }
  }

  if (selectTipo) {
    selectTipo.addEventListener('change', atualizarCamposPorTipo);
    atualizarCamposPorTipo();
  }

  // --- FUNÇÃO PARA ATUALIZAR OS CONTADORES (ESTATÍSTICAS) ---
  async function carregarEstatisticas() {
    try {
      const respAlimentos = await fetch('/api/alimentos');
      if (respAlimentos.ok) {
        const alimentos = await respAlimentos.json();
        const disponiveis = alimentos.filter(a => !a.status || a.status.toUpperCase() === 'DISPONIVEL');
        
        const elAlimentos = document.getElementById('sAlimentos');
        if (elAlimentos) elAlimentos.textContent = disponiveis.length;

        // Soma real de todas as quantidades de marmitas cadastradas
        const totalItensDoados = alimentos.reduce((soma, a) => soma + (Number(a.quantidade) || 0), 0);
        const elDoacoes = document.getElementById('sDoacoes');
        if (elDoacoes) elDoacoes.textContent = totalItensDoados;
      }

      const respUsuarios = await fetch('/api/usuarios');
      if (respUsuarios.ok) {
        const usuarios = await respUsuarios.json();
        const instituicoes = usuarios.filter(u => u.tipo && u.tipo.toUpperCase() === 'INSTITUICAO');
        
        const elInstituicoes = document.getElementById('sInstituicoes');
        if (elInstituicoes) elInstituicoes.textContent = instituicoes.length;
      }
    } catch (e) {
      console.error("Erro ao carregar estatísticas:", e);
    }
  }

  // --- FUNÇÃO PARA CARREGAR E EXIBIR OS ALIMENTOS DISPONÍVEIS ---
  async function carregarAlimentos() {
    const containerLista = document.getElementById('lista');
    if (!containerLista) {
      console.error("Elemento com id 'lista' não foi encontrado no HTML!");
      return;
    }

    try {
      containerLista.innerHTML = '<p class="muted">Carregando ofertas...</p>';
      
      const resposta = await fetch('/api/alimentos');
      if (!resposta.ok) throw new Error('Erro ao buscar alimentos no servidor.');

      const alimentos = await resposta.json();
      
      const disponiveis = alimentos.filter(a => 
        !a.status || a.status.toUpperCase() === 'DISPONIVEL'
      );

      if (disponiveis.length === 0) {
        containerLista.innerHTML = '<p class="muted">Nenhum alimento disponível no momento.</p>';
        return;
      }

      containerLista.innerHTML = disponiveis.map(a => `
        <div style="background: #fff; padding: 16px; margin-bottom: 12px; border-radius: 8px; border: 1px solid #cce3d4; box-shadow: 0 2px 4px rgba(0,0,0,0.05); display: flex; flex-direction: column; justify-content: space-between;">
          <div>
            <h3 style="margin-top: 0; color: #1b4332;">${a.nome}</h3>
            <p style="margin: 6px 0; color: #333;">${a.descricao || 'Sem descrição'}</p>
            <p style="margin: 6px 0;"><strong>Quantidade disponível:</strong> ${a.quantidade} ${a.unidade}</p>
            <p style="margin: 6px 0;"><strong>Validade:</strong> ${a.validade || 'Não informada'}</p>
            <p style="margin: 6px 0; font-size: 0.9em; color: #555;"><strong>Conservação:</strong> ${a.condicoesConservacao || 'Consumo imediato'}</p>
          </div>
          <button type="button" onclick="solicitarAlimento(${a.id})" style="margin-top: 12px; background-color: #2d6a4f; color: white; border: none; padding: 10px 14px; border-radius: 4px; cursor: pointer; font-weight: bold;">
            Solicitar Alimento
          </button>
        </div>
      `).join('');

    } catch (erro) {
      console.error(erro);
      containerLista.innerHTML = `<p class="muted" style="color: red;">Erro ao carregar ofertas: ${erro.message}</p>`;
    }
  }

  carregarAlimentos();
  carregarEstatisticas();

  if (formCadastro) {
    formCadastro.addEventListener('submit', async (e) => {
      e.preventDefault();
      mensagem.textContent = 'Salvando cadastro...';

      const tipo = selectTipo.value;
      let enderecoFinal = document.getElementById('endereco').value;

      if (tipo === 'INSTITUICAO') {
        const cidade = document.getElementById('cidadeDoacao').value.trim();
        const bairro = document.getElementById('bairroDoacao').value.trim();
        const horario = document.getElementById('horarioDoacao').value.trim();
        enderecoFinal += ` | Ponto de doação: ${bairro}, ${cidade} - Horário: ${horario}`;
      }

      try {
        const respUsuario = await fetch('/api/usuarios', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            nome: document.getElementById('nome').value,
            email: document.getElementById('email').value,
            telefone: document.getElementById('telefone').value,
            endereco: enderecoFinal,
            tipo: tipo
          })
        });

        if (!respUsuario.ok) throw new Error('Erro ao cadastrar organização.');
        const usuarioCriado = await respUsuario.json();

        if (tipo === 'DOADOR') {
          const nomeDoadora = document.getElementById('nomeDoadora').value.trim();
          const validade = document.getElementById('validadeComida').value;
          const qtd = parseInt(document.getElementById('qtdMarmitas').value, 10);

          const itensMarcados = Array.from(
            document.querySelectorAll('#listaItensMarmita input[type="checkbox"]:checked')
          ).map((cb) => cb.value);

          const textoOutros = document.getElementById('outrosItens').value.trim();
          if (textoOutros) {
            itensMarcados.push(`Outros: ${textoOutros}`);
          }

          if (qtd > 0) {
            const tituloDoacao = nomeDoadora
              ? `Marmita - ${nomeDoadora}`
              : `Marmita (${usuarioCriado.nome})`;

            const respAlimento = await fetch('/api/alimentos', {
              method: 'POST',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify({
                nome: tituloDoacao,
                descricao: `Itens da marmita: ${itensMarcados.join(', ') || 'Não especificado'}`,
                quantidade: qtd,
                unidade: 'marmitas',
                condicoesConservacao: 'Consumo imediato / Refrigeração',
                validade: validade || '2026-12-31',
                doador: { id: usuarioCriado.id }
              })
            });

            if (!respAlimento.ok) throw new Error('Organização salva, mas houve erro ao cadastrar a marmita.');
          }
        }

        mensagem.textContent = `Cadastro realizado com sucesso! ID gerado: ${usuarioCriado.id}`;
        formCadastro.reset();
        atualizarCamposPorTipo();
        
        carregarAlimentos();
        carregarEstatisticas();
      } catch (erro) {
        mensagem.textContent = `Erro: ${erro.message}`;
      }
    });
  }
});
// --- FUNÇÃO GLOBAL PARA SOLICITAR O ALIMENTO COM BOTÃO DO WHATSAPP ---
async function solicitarAlimento(alimentoId) {
  const idInstituicaoStr = prompt("Digite o ID da sua Instituição cadastrada:");
  if (!idInstituicaoStr) return;

  const instituicaoId = parseInt(idInstituicaoStr, 10);
  if (isNaN(instituicaoId)) {
    alert("ID de instituição inválido.");
    return;
  }

  const quantidadeStr = prompt("Digite a quantidade de marmitas que deseja solicitar:");
  if (!quantidadeStr) return;

  const quantidade = parseInt(quantidadeStr, 10);
  if (isNaN(quantidade) || quantidade <= 0) {
    alert("Quantidade inválida.");
    return;
  }

  try {
    // 1. Envia a solicitação para o Spring Boot
    const resposta = await fetch('/api/solicitacoes', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        alimento: { id: alimentoId },
        instituicao: { id: instituicaoId },
        quantidade: quantidade,
        quantidadeSolicitada: quantidade
      })
    });

    if (!resposta.ok) {
      const erroTexto = await resposta.text();
      throw new Error(erroTexto || 'Erro ao registrar solicitação.');
    }

    // 2. Busca dados atualizados para montar o link do WhatsApp
    const [respAlimentos, respUsuarios] = await Promise.all([
      fetch('/api/alimentos'),
      fetch('/api/usuarios')
    ]);

    const alimentos = await respAlimentos.json();
    const usuarios = await respUsuarios.json();

    const alimentoAtual = alimentos.find(a => a.id === alimentoId);
    const instituicaoAtual = usuarios.find(u => u.id === instituicaoId);
    const doadorAtual = usuarios.find(u => u.id === alimentoAtual?.doador?.id) || alimentoAtual?.doador;

    const telefoneDoador = doadorAtual?.telefone;

    if (telefoneDoador) {
      const telefoneLimpo = telefoneDoador.replace(/\D/g, '');
      const nomeDoador = doadorAtual?.nome || 'Doador';
      const nomeAlimento = alimentoAtual?.nome || 'Marmita';
      const nomeInstituicao = instituicaoAtual?.nome || 'Instituição';
      const enderecoInstituicao = instituicaoAtual?.endereco || 'Não informado';

      const textoMensagem = `Olá, ${nomeDoador}! A instituição *${nomeInstituicao}* acabou de solicitar *${quantidade}* unidade(s) de *${nomeAlimento}*.\n\n📍 *Dados para entrega/retirada:*\n${enderecoInstituicao}\n\nPodemos combinar a entrega?`;

      const urlWhatsApp = `https://api.whatsapp.com/send?phone=55${telefoneLimpo}&text=${encodeURIComponent(textoMensagem)}`;

      // 3. Exibe um botão verde grande na tela (sem sumir e sem ser bloqueado)
      const containerLista = document.getElementById('lista');
      if (containerLista) {
        containerLista.insertAdjacentHTML('beforebegin', `
          <div style="background: #d8f3dc; border: 2px solid #2d6a4f; padding: 20px; border-radius: 8px; margin-bottom: 20px; text-align: center; box-shadow: 0 4px 6px rgba(0,0,0,0.1);">
            <h3 style="margin-top: 0; color: #1b4332;">✅ Solicitação Registada com Sucesso!</h3>
            <p style="color: #333; margin-bottom: 15px;">Clique no botão abaixo para abrir a conversa com o doador <strong>${nomeDoador}</strong> no WhatsApp Web:</p>
            <a href="${urlWhatsApp}" target="_blank" style="display: inline-block; background: #25D366; color: white; padding: 14px 24px; border-radius: 6px; text-decoration: none; font-weight: bold; font-size: 1.1em;">
              📲 Abrir WhatsApp do Doador (${nomeDoador})
            </a>
          </div>
        `);
      }
    } else {
      alert('Solicitação registada, mas o doador não tem telefone cadastrado.');
      carregarAlimentos();
      carregarEstatisticas();
    }

  } catch (erro) {
    alert(`Erro: ${erro.message}`);
    console.error("Detalhes do erro:", erro);
  }
}