document.addEventListener("DOMContentLoaded", () => {
    // --- STORES & DATA STATE ---
    let barrios = [];
    let unidades = [];
    let personas = [];
    let reclamos = [];
    let visitasActivas = [];

    // --- DOM ELEMENTS ---
    const roleSelect = document.getElementById("role-select");
    const filterBarrioSelect = document.getElementById("filter-barrio-list");
    const panels = {
        ADMINISTRADOR: document.getElementById("panel-admin"),
        RESIDENTE: document.getElementById("panel-residente"),
        GUARDIA: document.getElementById("panel-guardia"),
        PROVEEDOR: document.getElementById("panel-proveedor")
    };

    // --- SYSTEM CONSOLE LOGGING ---
    const consoleLog = document.getElementById("notification-console");

    function logSystem(message, type = "system") {
        // Enrutar todas las notificaciones al canal global seleccionado
        if (type === "email" || type === "sms" || type === "push") {
            const activeChannel = document.getElementById("active-notification-channel").value;
            // Reemplazar la etiqueta inicial [Email Notification], [Push Notification], etc. con el formato del canal activo
            message = message.replace(/^\[(Email|SMS|Push)\s+Notification\]/i, `[${activeChannel.toUpperCase()} Notification]`);
            type = activeChannel;
        }

        const time = new Date().toLocaleTimeString();
        const entry = document.createElement("div");
        entry.className = `console-entry ${type}`;
        
        const timeSpan = document.createElement("span");
        timeSpan.className = "console-time";
        timeSpan.textContent = `[${time}] `;
        
        const textSpan = document.createElement("span");
        textSpan.textContent = message;
        
        entry.appendChild(timeSpan);
        entry.appendChild(textSpan);
        consoleLog.appendChild(entry);
        consoleLog.scrollTop = consoleLog.scrollHeight;
    }

    // --- TOAST NOTIFICATIONS ---
    function showToast(message, type = "success") {
        let container = document.getElementById("toast-container");
        if (!container) {
            container = document.createElement("div");
            container.id = "toast-container";
            container.className = "toast-container";
            document.body.appendChild(container);
        }
        
        const toast = document.createElement("div");
        toast.className = `toast-message ${type}`;
        toast.textContent = message;
        container.appendChild(toast);
        
        setTimeout(() => {
            toast.classList.add("hide");
            setTimeout(() => {
                toast.remove();
            }, 300);
        }, 3500);
    }

    // Validaciones rápidas del front
    function esDniValido(dni) {
        return /^\d{8}$/.test(dni);
    }

    function esEmailComValido(email) {
        return /^[^\s@]+@[^\s@]+\.com$/i.test(email);
    }

    function validarDniAntesDeEnviar(dni, contexto = "DNI") {
        if (!esDniValido(dni)) {
            showToast(`${contexto} debe tener exactamente 8 números`, "error");
            return false;
        }
        return true;
    }

    function validarEmailAntesDeEnviar(email) {
        if (!esEmailComValido(email)) {
            showToast("El email debe tener formato usuario@dominio.com", "error");
            return false;
        }
        return true;
    }

    // --- ROLE SWITCHER LOGIC ---
    roleSelect.addEventListener("change", (e) => {
        const selectedRole = e.target.value;
        Object.keys(panels).forEach(role => {
            if (role === selectedRole) {
                panels[role].classList.add("active");
            } else {
                panels[role].classList.remove("active");
            }
        });
        logSystem(`Vista cambiada a: ${selectedRole}`, "system");
        cargarDatosPorRol(selectedRole);
    });

    // --- API UTILITIES (FETCH) ---
    async function apiRequest(url, method = "GET", body = null) {
        const options = {
            method,
            headers: {
                "Content-Type": "application/json"
            }
        };
        if (body) {
            options.body = JSON.stringify(body);
        }
        try {
            const response = await fetch(url, options);
            if (!response.ok) {
                const errText = await response.text();
                let userMessage = `Error de servidor: ${response.status}`;
                try {
                    const parsedErr = JSON.parse(errText);
                    if (parsedErr.mensaje) {
                        userMessage = parsedErr.mensaje;
                        // Guarda defensiva por si llega el error SQL directo en 'mensaje'
                        if (userMessage.includes("personas_dni_key") || userMessage.includes("Key (dni)=")) {
                            userMessage = "El DNI ingresado ya se encuentra registrado para otra persona.";
                        } else if (userMessage.includes("personas_email_key")) {
                            userMessage = "El email ingresado ya se encuentra registrado para otra persona.";
                        }
                    } else if (parsedErr.validationErrors) {
                        userMessage = Object.entries(parsedErr.validationErrors)
                            .map(([field, msg]) => `${field}: ${msg}`)
                            .join(", ");
                    } else if (parsedErr.error) {
                        userMessage = parsedErr.error;
                    }
                } catch (parseEx) {
                    if (errText) {
                        userMessage = errText;
                    }
                }
                throw new Error(userMessage);
            }
            if (response.status === 204) return null;
            const text = await response.text();
            return text ? JSON.parse(text) : null;
        } catch (error) {
            logSystem(`Error API (${url}): ${error.message}`, "system");
            console.error(error);
            showToast(`Error: ${error.message}`, "error");
            throw error;
        }
    }

    async function cargarDatosPorRol(rol) {
        // Carga secuencial llamando a las funciones que también rellenan los desplegables de formularios
        await cargarBarrios();
        await cargarUnidades();
        await cargarPersonas();
        
        // Obtener visitas activas de forma global para filtrar personas ya ingresadas
        try {
            const visitas = await apiRequest("/api/visitas");
            visitasActivas = visitas.filter(v => v.estado === "EN_CURSO");
        } catch (e) {
            console.error("Error al obtener visitas activas para filtro:", e);
        }

        // Rellenar selector de filtro de barrios en Admin si está vacío
        actualizarSelectFiltroBarrios();

        // Renderizado de tablas principales
        renderTablaUnidades();
        renderTablaPersonas();
        
        // Inicializar simulador
        inicializarSelectoresContexto();

        if (rol === "ADMINISTRADOR") {
            await cargarReclamosAdmin();
        } else if (rol === "GUARDIA") {
            renderTablaVisitasActivas();
        }
    }

    // Actualizar el selector de filtro por barrio
    function actualizarSelectFiltroBarrios() {
        const prevVal = filterBarrioSelect.value || "ALL";
        filterBarrioSelect.innerHTML = '<option value="ALL">Todos los barrios</option>';
        barrios.forEach(b => {
            const opt = document.createElement("option");
            opt.value = b.id;
            opt.textContent = b.nombre;
            if (b.id == prevVal) opt.selected = true;
            filterBarrioSelect.appendChild(opt);
        });
    }

    // Event listener para filtrar las listas por Barrio
    filterBarrioSelect.addEventListener("change", () => {
        renderTablaUnidades();
        renderTablaPersonas();
    });

    // Cargar Barrios para el formulario
    async function cargarBarrios() {
        try {
            barrios = await apiRequest("/api/barrios");
            const selectUnidad = document.getElementById("unidad-barrio");
            selectUnidad.innerHTML = '<option value="" disabled selected>Seleccione un barrio...</option>';
            barrios.forEach(b => {
                const opt = document.createElement("option");
                opt.value = b.id;
                opt.textContent = b.nombre;
                selectUnidad.appendChild(opt);
            });
            actualizarSelectFiltroBarrios();
        } catch (e) {}
    }

    // Cargar Unidades para el formulario
    async function cargarUnidades() {
        try {
            unidades = await apiRequest("/api/unidades");
            const selectPersona = document.getElementById("persona-unidad");
            selectPersona.innerHTML = '<option value="" disabled selected>Seleccione unidad...</option>';
            unidades.forEach(u => {
                const opt = document.createElement("option");
                opt.value = u.id;
                const barrio = barrios.find(b => b.id === u.barrioId);
                opt.textContent = `${u.identificador} (${barrio ? barrio.nombre : 'Barrio #' + u.barrioId})`;
                selectPersona.appendChild(opt);
            });
        } catch (e) {}
    }

    // Cargar Personas
    async function cargarPersonas() {
        try {
            const listRes = await apiRequest("/api/residentes") || [];
            const listPers = await apiRequest("/api/personal") || [];
            let listVis = [];
            try {
                listVis = await apiRequest("/api/visitantes") || [];
            } catch (err) {
                console.error("No se pudieron cargar los visitantes:", err);
            }
            personas = [
                ...listRes.map(r => ({ ...r, tipo: "RESIDENTE" })),
                ...listPers,
                ...listVis.map(v => ({ ...v, tipo: "VISITANTE" }))
            ];
        } catch (e) {}
    }

    // --- RENDERIZADO DE TABLAS ---
    function renderTablaUnidades() {
        const tbody = document.querySelector("#tabla-unidades tbody");
        tbody.innerHTML = "";

        const selectedBarrioId = filterBarrioSelect.value;

        // Filtrar unidades
        const unidadesFiltradas = selectedBarrioId === "ALL" 
            ? unidades 
            : unidades.filter(u => u.barrioId == selectedBarrioId);

        unidadesFiltradas.forEach(u => {
            const tr = document.createElement("tr");
            const barrio = barrios.find(b => b.id === u.barrioId);
            
            const resAsociados = personas.filter(p => p.tipo === "RESIDENTE" && p.unidadFuncionalId === u.id);
            const resNombres = resAsociados.map(r => `${r.nombre} ${r.apellido}`).join(", ") || "<span class='text-muted'>Ninguno</span>";
            
            const btnClass = u.habilitada ? "badge-success" : "badge-danger";
            const btnText = u.habilitada ? "Activa" : "Suspendida";

            tr.innerHTML = `
                <td>${u.id}</td>
                <td>${u.identificador}</td>
                <td><span class="badge badge-muted">${u.tipoUnidad}</span></td>
                <td>${barrio ? barrio.nombre : 'Barrio #' + u.barrioId}</td>
                <td>${resNombres}</td>
                <td>
                    <button class="badge ${btnClass}" style="cursor: pointer; border: none; font-family: inherit; font-size: 0.75rem;" onclick="toggleUnidadHabilitacion(${u.id})">
                        ${btnText}
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    }

    function renderTablaPersonas() {
        const tbody = document.querySelector("#tabla-personas tbody");
        tbody.innerHTML = "";

        const selectedBarrioId = filterBarrioSelect.value;

        // Dividir entre residentes del barrio y personal general
        const residentes = personas.filter(p => p.tipo === "RESIDENTE");
        const personalGeneral = personas.filter(p => p.tipo !== "RESIDENTE");

        // Filtrar residentes según el barrio de su unidad funcional
        const residentesFiltrados = residentes.filter(p => {
            if (selectedBarrioId === "ALL") return true;
            const uf = unidades.find(u => u.id === p.unidadFuncionalId);
            return uf && uf.barrioId == selectedBarrioId;
        });

        // 1. Mostrar Residentes Filtrados
        residentesFiltrados.forEach(p => {
            const tr = document.createElement("tr");
            const uf = unidades.find(u => u.id === p.unidadFuncionalId);
            const b = uf ? barrios.find(barr => barr.id === uf.barrioId) : null;
            const asociacion = uf ? `Unidad ${uf.identificador} (${b ? b.nombre : 'Barrio #' + uf.barrioId})` : "Unidad no asignada";
            
            const btnClass = p.habilitado ? "badge-success" : "badge-danger";
            const btnText = p.habilitado ? "Habilitado" : "Bloqueado";

            tr.innerHTML = `
                <td>${p.nombre} ${p.apellido}</td>
                <td>${p.dni}</td>
                <td><span class="badge badge-success">RESIDENTE</span></td>
                <td>${asociacion}</td>
                <td>
                    <button class="badge ${btnClass}" style="cursor: pointer; border: none; font-family: inherit; font-size: 0.75rem;" onclick="togglePersonaHabilitacion(${p.id})">
                        ${btnText}
                    </button>
                </td>
            `;
            tbody.appendChild(tr);
        });

        // 2. Mostrar Personal General (separados o en su propio grupo)
        if (personalGeneral.length > 0) {
            // Añadir fila divisoria en la tabla para mejor claridad
            const rowHeader = document.createElement("tr");
            rowHeader.innerHTML = `
                <td colspan="5" style="background: rgba(255,255,255,0.02); font-weight: 600; font-size: 0.8rem; color: var(--text-secondary); text-transform: uppercase; letter-spacing: 0.5px; padding: 0.5rem 1rem;">
                    ⚙️ Personal y Proveedores
                </td>
            `;
            tbody.appendChild(rowHeader);

            personalGeneral.forEach(p => {
                const tr = document.createElement("tr");
                let asociacion = "";
                if (p.tipo === "PROVEEDOR") {
                    asociacion = p.tipoServicio ? `Proveedor Externo: ${p.tipoServicio}` : "Proveedor Externo";
                } else if (p.tipo === "MANTENIMIENTO") {
                    asociacion = "Mantenimiento Interno";
                } else if (p.tipo === "SEGURIDAD") {
                    asociacion = "Seguridad / Portería";
                } else {
                    asociacion = "Administración";
                }
                
                const btnClass = p.habilitado ? "badge-success" : "badge-danger";
                const btnText = p.habilitado ? "Habilitado" : "Bloqueado";

                tr.innerHTML = `
                    <td>${p.nombre} ${p.apellido}</td>
                    <td>${p.dni}</td>
                    <td><span class="badge badge-process">${p.tipo}</span></td>
                    <td>${asociacion}</td>
                    <td>
                        <button class="badge ${btnClass}" style="cursor: pointer; border: none; font-family: inherit; font-size: 0.75rem;" onclick="togglePersonaHabilitacion(${p.id})">
                            ${btnText}
                        </button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        }
    }

    // --- ACCIONES FORMULARIOS ADMINISTRADOR ---
    document.getElementById("form-barrio").addEventListener("submit", async (e) => {
        e.preventDefault();
        const nombre = document.getElementById("barrio-nombre").value.trim();
        const direccion = document.getElementById("barrio-direccion").value.trim();
        
        try {
            const result = await apiRequest("/api/barrios", "POST", { nombre, direccion });
            logSystem(`Barrio creado: ${result.nombre} (ID: ${result.id})`, "system");
            showToast(`Barrio "${result.nombre}" creado con éxito.`, "success");
            document.getElementById("form-barrio").reset();
            await cargarDatosPorRol("ADMINISTRADOR");
        } catch (error) {}
    });

    const formUnidad = document.getElementById("form-unidad");
    if (formUnidad) {
        formUnidad.addEventListener("submit", async (e) => {
            e.preventDefault();
            const barrioId = document.getElementById("unidad-barrio").value;
            const identificador = document.getElementById("unidad-identificador").value.trim();
            const tipoUnidad = document.getElementById("unidad-tipo").value;

            try {
                const result = await apiRequest("/api/unidades", "POST", {
                    barrioId: parseInt(barrioId),
                    identificador,
                    tipoUnidad
                });
                logSystem(`Unidad registrada: ${result.identificador} (ID: ${result.id})`, "system");
                showToast(`Unidad "${result.identificador}" registrada con éxito.`, "success");
                document.getElementById("form-unidad").reset();
                await cargarDatosPorRol("ADMINISTRADOR");
            } catch (error) {}
        });
    }

    const formPersona = document.getElementById("form-persona");
    const selectTipoPersona = document.getElementById("persona-tipo");
    const groupUnidad = document.getElementById("group-residente-unidad");
    const groupServicio = document.getElementById("group-proveedor-servicio");

    selectTipoPersona.addEventListener("change", (e) => {
        const val = e.target.value;
        if (val === "RESIDENTE") {
            groupUnidad.style.display = "flex";
            groupServicio.style.display = "none";
        } else if (val === "PROVEEDOR") {
            groupUnidad.style.display = "none";
            groupServicio.style.display = "flex";
        } else {
            groupUnidad.style.display = "none";
            groupServicio.style.display = "none";
        }
    });

    formPersona.addEventListener("submit", async (e) => {
        e.preventDefault();
        const tipo = selectTipoPersona.value;
        const nombre = document.getElementById("persona-nombre").value.trim();
        const apellido = document.getElementById("persona-apellido").value.trim();
        const dni = document.getElementById("persona-dni").value.trim();
        const email = document.getElementById("persona-email").value.trim();

        if (!validarDniAntesDeEnviar(dni) || !validarEmailAntesDeEnviar(email)) {
            return;
        }

        try {
            let result;
            if (tipo === "RESIDENTE") {
                const unidadFuncionalId = document.getElementById("persona-unidad").value;
                result = await apiRequest("/api/residentes", "POST", {
                    nombre, apellido, dni, email,
                    unidadFuncionalId: parseInt(unidadFuncionalId)
                });
            } else {
                const tipoServicio = document.getElementById("persona-servicio").value.trim();
                result = await apiRequest("/api/personal", "POST", {
                    nombre, apellido, dni, email, tipo,
                    tipoServicio: tipo === "PROVEEDOR" ? tipoServicio : null
                });
            }
            logSystem(`Persona registrada via Factory: ${result.nombre} ${result.apellido} (Tipo: ${tipo})`, "system");
            showToast(`${result.nombre} ${result.apellido} registrado con éxito.`, "success");
            formPersona.reset();
            groupUnidad.style.display = "flex";
            groupServicio.style.display = "none";
            await cargarDatosPorRol("ADMINISTRADOR");
        } catch (error) {}
    });

    // --- CONTEXT SELECTORS & INITIALIZATION (SIMULATOR) ---
    function inicializarSelectoresContexto() {
        // --- RESIDENT PANEL ---
        const activeResSelect = document.getElementById("active-residente-select");
        const formResSelect = document.getElementById("reclamo-residente");
        const autResSelect = document.getElementById("aut-residente");
        
        const listRes = personas.filter(p => p.tipo === "RESIDENTE");
        
        const prevResVal = activeResSelect.value;
        activeResSelect.innerHTML = listRes.length ? "" : '<option value="" disabled>No hay residentes registrados</option>';
        
        listRes.forEach((r, idx) => {
            const opt = document.createElement("option");
            opt.value = r.id;
            const uf = unidades.find(u => u.id === r.unidadFuncionalId);
            opt.textContent = `${r.nombre} ${r.apellido} (UF: ${uf ? uf.identificador : 'ID #' + r.unidadFuncionalId})`;
            if (prevResVal && prevResVal == r.id) {
                opt.selected = true;
            } else if (!prevResVal && idx === 0) {
                opt.selected = true;
            }
            activeResSelect.appendChild(opt);
        });
        
        const syncResident = () => {
            const activeVal = activeResSelect.value;
            if (activeVal) {
                formResSelect.innerHTML = `<option value="${activeVal}" selected>${activeVal}</option>`;
                autResSelect.innerHTML = `<option value="${activeVal}" selected>${activeVal}</option>`;
                cargarReclamosResidente(activeVal);
            }
        };
        activeResSelect.onchange = syncResident;
        syncResident();

        // --- GUARDIA PANEL ---
        const activeGuardiaSelect = document.getElementById("active-guardia-select");
        const formGuardiaSelect = document.getElementById("checkin-seguridad");
        
        const listGuardias = personas.filter(p => p.tipo === "SEGURIDAD");
        const prevGuardiaVal = activeGuardiaSelect.value;
        activeGuardiaSelect.innerHTML = listGuardias.length ? "" : '<option value="" disabled>No hay guardias de seguridad</option>';
        
        listGuardias.forEach((g, idx) => {
            const opt = document.createElement("option");
            opt.value = g.id;
            opt.textContent = `${g.nombre} ${g.apellido}`;
            if (prevGuardiaVal && prevGuardiaVal == g.id) {
                opt.selected = true;
            } else if (!prevGuardiaVal && idx === 0) {
                opt.selected = true;
            }
            activeGuardiaSelect.appendChild(opt);
        });
        
        const syncGuardia = () => {
            const activeVal = activeGuardiaSelect.value;
            if (activeVal) {
                formGuardiaSelect.innerHTML = `<option value="${activeVal}" selected>${activeVal}</option>`;
            }
        };
        activeGuardiaSelect.onchange = syncGuardia;
        syncGuardia();

        // --- PROVEEDOR PANEL ---
        const activeProvSelect = document.getElementById("active-proveedor-select");
        const filterProvSelect = document.getElementById("proveedor-filtro");
        
        const listProvs = personas.filter(p => p.tipo === "PROVEEDOR" || p.tipo === "MANTENIMIENTO");
        const prevProvVal = activeProvSelect.value;
        activeProvSelect.innerHTML = listProvs.length ? "" : '<option value="" disabled>No hay operarios registrados</option>';
        
        listProvs.forEach((op, idx) => {
            const opt = document.createElement("option");
            opt.value = op.id;
            // Distinción clara de roles de mantenimiento
            const tagRol = op.tipo === "MANTENIMIENTO" ? "Mantenimiento Interno" : `Proveedor Externo: ${op.tipoServicio}`;
            opt.textContent = `${op.nombre} ${op.apellido} (${tagRol})`;
            if (prevProvVal && prevProvVal == op.id) {
                opt.selected = true;
            } else if (!prevProvVal && idx === 0) {
                opt.selected = true;
            }
            activeProvSelect.appendChild(opt);
        });
        
        const syncProveedor = () => {
            const activeVal = activeProvSelect.value;
            if (activeVal) {
                filterProvSelect.innerHTML = `<option value="${activeVal}" selected>${activeVal}</option>`;
                filterProvSelect.dispatchEvent(new Event("change"));
            }
        };
        activeProvSelect.onchange = syncProveedor;
        syncProveedor();

        // --- PORTERIA: SELECT ESTRATEGICO DE PERSONA ---
        const checkinPersonaSelect = document.getElementById("checkin-persona-select");
        const dniInput = document.getElementById("checkin-visitante-dni");
        
        checkinPersonaSelect.innerHTML = '<option value="" disabled selected>Seleccione la persona que ingresa...</option>';
        
        // Obtener IDs de personas que ya están adentro del barrio
        const idsAdentro = visitasActivas.map(v => v.visitanteId);
        
        // Grupo Residentes
        const groupRes = document.createElement("optgroup");
        groupRes.label = "Residentes";
        personas.filter(p => p.tipo === "RESIDENTE" && !idsAdentro.includes(p.id)).forEach(p => {
            const opt = document.createElement("option");
            opt.value = p.id;
            opt.dataset.dni = p.dni;
            opt.textContent = `${p.nombre} ${p.apellido} (DNI: ${p.dni})`;
            groupRes.appendChild(opt);
        });
        checkinPersonaSelect.appendChild(groupRes);

        // Grupo Personal y Proveedores (Mantenimiento / Seguridad)
        const groupStaff = document.createElement("optgroup");
        groupStaff.label = "Personal y Proveedores";
        personas.filter(p => p.tipo !== "RESIDENTE" && p.tipo !== "VISITANTE" && !idsAdentro.includes(p.id)).forEach(p => {
            const opt = document.createElement("option");
            opt.value = p.id;
            opt.dataset.dni = p.dni;
            const rolLabel = p.tipo === "MANTENIMIENTO" ? "Mantenimiento Interno" : p.tipo;
            opt.textContent = `${p.nombre} ${p.apellido} (Rol: ${rolLabel} - DNI: ${p.dni})`;
            groupStaff.appendChild(opt);
        });
        checkinPersonaSelect.appendChild(groupStaff);

        // Grupo Visitantes Externos
        const groupVis = document.createElement("optgroup");
        groupVis.label = "Visitantes Externos Autorizados";
        personas.filter(p => p.tipo === "VISITANTE" && !idsAdentro.includes(p.id)).forEach(p => {
            const opt = document.createElement("option");
            opt.value = p.id;
            opt.dataset.dni = p.dni;
            opt.textContent = `${p.nombre} ${p.apellido} (DNI: ${p.dni})`;
            groupVis.appendChild(opt);
        });
        checkinPersonaSelect.appendChild(groupVis);

        checkinPersonaSelect.onchange = () => {
            const selectedOpt = checkinPersonaSelect.options[checkinPersonaSelect.selectedIndex];
            if (selectedOpt && selectedOpt.dataset.dni) {
                dniInput.value = selectedOpt.dataset.dni;
            }
        };
    }

    // --- RECLAMOS RESIDENTE ---
    async function cargarReclamosResidente(residenteId) {
        try {
            const myReclamos = await apiRequest(`/api/reclamos?residenteId=${residenteId}`);
            renderTablaReclamosResidente(myReclamos);
        } catch (e) {
            console.log("Error cargando reclamos del residente");
        }
    }

    function renderTablaReclamosResidente(myReclamos) {
        const tbody = document.querySelector("#tabla-reclamos-residente tbody");
        tbody.innerHTML = "";
        if (!myReclamos || myReclamos.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="text-muted" style="text-align: center;">No tienes reclamos registrados.</td></tr>`;
            return;
        }
        myReclamos.forEach(r => {
            const tr = document.createElement("tr");
            const badgeClass = {
                PENDIENTE: "badge-pending",
                EN_PROCESO: "badge-process",
                RESUELTO: "badge-success",
                CERRADO: "badge-muted",
                CANCELADO: "badge-danger"
            }[r.estado] || "badge-muted";

            tr.innerHTML = `
                <td>#${r.id}</td>
                <td>${r.descripcion}</td>
                <td><span class="badge">${r.prioridad}</span></td>
                <td><span class="badge ${badgeClass}">${r.estado}</span></td>
                <td>${r.responsableNombreCompleto || '<span class="text-muted">Sin asignar</span>'}</td>
            `;
            tbody.appendChild(tr);
        });
    }

    // --- RECLAMOS ADMINISTRADOR (TABLA MESA) ---
    async function cargarReclamosAdmin() {
        try {
            reclamos = await apiRequest("/api/reclamos");
            renderTablaReclamosAdmin();
        } catch (e) {}
    }

    function renderTablaReclamosAdmin() {
        const tbody = document.querySelector("#tabla-reclamos-admin tbody");
        tbody.innerHTML = "";
        
        const operarios = personas.filter(p => p.tipo === "PROVEEDOR" || p.tipo === "MANTENIMIENTO");
        
        reclamos.forEach(r => {
            const tr = document.createElement("tr");
            
            const badgeClass = {
                PENDIENTE: "badge-pending",
                EN_PROCESO: "badge-process",
                RESUELTO: "badge-success",
                CERRADO: "badge-muted",
                CANCELADO: "badge-danger"
            }[r.estado] || "badge-muted";

            const res = personas.find(p => p.id === r.residenteId);
            const resName = res ? `${res.nombre} ${res.apellido}` : `Residente #${r.residenteId}`;

            tr.innerHTML = `
                <td>#${r.id}</td>
                <td>${resName}</td>
                <td><strong>${r.tipoReclamo}</strong><br><small>${r.descripcion}</small></td>
                <td><span class="badge">${r.prioridad}</span></td>
                <td><span class="badge ${badgeClass}">${r.estado}</span></td>
                <td>${r.responsableNombreCompleto ? r.responsableNombreCompleto : '<span class="text-muted">Sin asignar</span>'}</td>
                <td>
                    ${r.estado === 'PENDIENTE' ? `
                        <div style="display: flex; gap: 0.5rem; align-items: center;">
                            <select class="form-control select-responsable-inline" data-id="${r.id}" style="padding: 0.25rem 0.5rem; font-size: 0.75rem; min-width: 170px; background: rgba(15, 23, 42, 0.8);">
                                <option value="" disabled selected>Asignar a...</option>
                                ${operarios.map(op => {
                                    const descRol = op.tipo === "MANTENIMIENTO" ? "Interno" : `Ext: ${op.tipoServicio}`;
                                    return `<option value="${op.id}">${op.nombre} ${op.apellido} (${descRol})</option>`;
                                }).join('')}
                            </select>
                            <button class="btn btn-primary btn-sm btn-asignar-inline" data-id="${r.id}" style="padding: 0.25rem 0.5rem; font-size: 0.75rem;">Confirmar</button>
                        </div>
                    ` : ''}
                </td>
            `;
            tbody.appendChild(tr);
        });

        // Event listener para asignación inline
        document.querySelectorAll(".btn-asignar-inline").forEach(btn => {
            btn.addEventListener("click", async (e) => {
                const reclamoId = e.target.getAttribute("data-id");
                const select = document.querySelector(`.select-responsable-inline[data-id="${reclamoId}"]`);
                const responsableId = select.value;
                
                if (!responsableId) {
                    showToast("Por favor seleccione un operario del desplegable.", "warning");
                    return;
                }

                try {
                    const res = await apiRequest(`/api/reclamos/${reclamoId}/asignar`, "PUT", {
                        responsableId: parseInt(responsableId)
                    });
                    const op = personas.find(p => p.id == responsableId);
                    const labelRol = op.tipo === "MANTENIMIENTO" ? "interno" : "proveedor externo";
                    
                    logSystem(`Reclamo #${reclamoId} asignado al responsable ID #${responsableId} (${labelRol})`, "system");
                    logSystem(`[Email Notification] Reclamo #${reclamoId} asignado al responsable.`, "email");
                    showToast(`Reclamo #${reclamoId} asignado correctamente.`, "success");
                    cargarReclamosAdmin();
                } catch (err) {}
            });
        });
    }

    // --- PORTERIA / VISITAS ---
    async function cargarVisitasActivas() {
        try {
            const visitas = await apiRequest("/api/visitas");
            visitasActivas = visitas.filter(v => v.estado === "EN_CURSO");
            renderTablaVisitasActivas();
        } catch (e) {}
    }

    function renderTablaVisitasActivas() {
        const tbody = document.querySelector("#tabla-visitas-activas tbody");
        tbody.innerHTML = "";
        if (visitasActivas.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="text-muted" style="text-align: center;">No hay ingresos/visitas activas dentro del complejo.</td></tr>`;
            return;
        }
        visitasActivas.forEach(v => {
            const tr = document.createElement("tr");
            
            // Encontrar nombre de persona real ingresada
            const persObj = personas.find(p => p.id === v.visitanteId);
            const pName = persObj ? `${persObj.nombre} ${persObj.apellido} (${persObj.tipo})` : "Persona";

            tr.innerHTML = `
                <td>#${v.id}</td>
                <td>${pName} - DNI: ${v.dniVisitante || "s/d"}</td>
                <td>${new Date(v.fechaIngreso).toLocaleString()}</td>
                <td>Portería</td>
                <td><button class="btn btn-danger btn-sm btn-checkout" data-vis="${v.visitanteId}" style="padding: 0.25rem 0.5rem; font-size: 0.75rem;">Registrar Salida</button></td>
            `;
            tbody.appendChild(tr);
        });

        document.querySelectorAll(".btn-checkout").forEach(btn => {
            btn.addEventListener("click", async (e) => {
                const visitanteId = e.target.getAttribute("data-vis");
                try {
                    await apiRequest("/api/visitas/salida", "POST", { visitanteId: parseInt(visitanteId) });
                    logSystem(`Salida de Persona ID #${visitanteId} registrada en portería`, "system");
                    showToast("Salida registrada con éxito.", "success");
                    await cargarDatosPorRol("GUARDIA");
                } catch (err) {}
            });
        });
    }

    // --- PROVEEDOR FILTRO & ACCIONES ---
    document.getElementById("proveedor-filtro").addEventListener("change", async (e) => {
        const id = e.target.value;
        if (!id) return;
        try {
            const list = await apiRequest(`/api/reclamos?responsableId=${id}`);
            renderTablaTareasProveedor(list, id);
        } catch (err) {}
    });

    function renderTablaTareasProveedor(tareas, responsableId) {
        const tbody = document.querySelector("#tabla-tareas-proveedor tbody");
        tbody.innerHTML = "";
        if (tareas.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="text-muted" style="text-align: center;">No tienes tareas de mantenimiento asignadas.</td></tr>`;
            return;
        }
        tareas.forEach(t => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>#${t.id}</td>
                <td>${t.descripcion}</td>
                <td><span class="badge">${t.prioridad}</span></td>
                <td><span class="badge badge-process">${t.estado}</span></td>
                <td class="td-acciones-tarea" data-id="${t.id}">
                    ${t.estado === 'EN_PROCESO' ? `<button class="btn btn-success btn-sm btn-resolver" data-id="${t.id}" style="padding: 0.25rem 0.5rem; font-size: 0.75rem;">Resolver</button>` : ''}
                    ${t.estado === 'PENDIENTE' ? `<span class="text-muted">Acepte la tarea en el panel de Admin</span>` : ''}
                    ${t.estado === 'RESUELTO' || t.estado === 'CERRADO' ? `<span class="badge badge-success">Completado</span>` : ''}
                </td>
            `;
            tbody.appendChild(tr);
        });

        // Event listener para resolver tareas INLINE (evitando prompt de Chrome)
        document.querySelectorAll(".btn-resolver").forEach(btn => {
            btn.addEventListener("click", (e) => {
                const id = e.target.getAttribute("data-id");
                const tdAcciones = document.querySelector(`.td-acciones-tarea[data-id="${id}"]`);
                
                // Mostrar formulario inline en el TD
                tdAcciones.innerHTML = `
                    <div style="display: flex; flex-direction: column; gap: 0.25rem; align-items: flex-start;">
                        <input type="text" class="form-control input-obs-inline" placeholder="Escriba observación..." style="padding: 0.25rem 0.5rem; font-size: 0.75rem; min-width: 140px; background: rgba(15, 23, 42, 0.9);">
                        <div style="display: flex; gap: 0.25rem; margin-top: 0.25rem;">
                            <button class="btn btn-success btn-sm btn-confirmar-resolver" data-id="${id}" style="padding: 0.15rem 0.35rem; font-size: 0.7rem;">Guardar</button>
                            <button class="btn btn-danger btn-sm btn-cancelar-resolver" data-id="${id}" style="padding: 0.15rem 0.35rem; font-size: 0.7rem;">X</button>
                        </div>
                    </div>
                `;

                // Botón cancelar
                tdAcciones.querySelector(".btn-cancelar-resolver").onclick = () => {
                    // Recargar tareas para restaurar el botón original
                    document.getElementById("proveedor-filtro").dispatchEvent(new Event("change"));
                };

                // Botón confirmar
                tdAcciones.querySelector(".btn-confirmar-resolver").onclick = async () => {
                    const obsInput = tdAcciones.querySelector(".input-obs-inline");
                    const obsValue = obsInput.value.trim() || "Trabajo de mantenimiento finalizado.";
                    
                    try {
                        await apiRequest(`/api/reclamos/${id}/estado`, "PUT", {
                            nuevoEstado: "RESUELTO",
                            observacion: obsValue
                        });
                        
                        logSystem(`Reclamo #${id} resuelto por responsable ID #${responsableId}`, "system");
                        logSystem(`[Email Notification] Reclamo #${id} ha sido RESUELTO.`, "email");
                        logSystem(`[Push Notification] Alerta de resolución enviada al residente del Reclamo #${id}`, "push");
                        showToast(`Tarea del Reclamo #${id} resuelta con éxito.`, "success");
                        
                        // Recargar tareas
                        document.getElementById("proveedor-filtro").dispatchEvent(new Event("change"));
                    } catch (err) {}
                };
            });
        });
    }

    // --- FORM SUBS ---
    document.getElementById("form-reclamo").addEventListener("submit", async (e) => {
        e.preventDefault();
        const residenteId = document.getElementById("reclamo-residente").value;
        const tipoReclamo = document.getElementById("reclamo-tipo").value;
        const prioridad = document.getElementById("reclamo-prioridad").value;
        const descripcion = document.getElementById("reclamo-descripcion").value.trim();

        try {
            const res = await apiRequest("/api/reclamos", "POST", {
                residenteId: parseInt(residenteId),
                tipoReclamo,
                prioridad,
                descripcion
            });
            logSystem(`Reclamo #${res.id} creado (PENDIENTE) por residente ID #${residenteId}`, "system");
            logSystem(`[Push Notification] Nuevo reclamo #${res.id} registrado para Administración`, "push");
            showToast(`Reclamo #${res.id} enviado con éxito.`, "success");
            document.getElementById("form-reclamo").reset();
            
            // Recargar datos para el residente
            document.getElementById("active-residente-select").dispatchEvent(new Event("change"));
        } catch (err) {}
    });

    document.getElementById("form-autorizar").addEventListener("submit", async (e) => {
        e.preventDefault();
        const residenteId = document.getElementById("aut-residente").value;
        const visitanteNombre = document.getElementById("aut-visitante-nombre").value.trim();
        const visitanteDni = document.getElementById("aut-visitante-dni").value.trim();
        const desdeVal = document.getElementById("aut-desde").value;

        if (!validarDniAntesDeEnviar(visitanteDni, "El DNI del visitante")) {
            return;
        }

        // Calcular fechaHasta de forma automática sumando 24 horas a desdeVal
        const desdeDate = new Date(desdeVal);
        const hastaDate = new Date(desdeDate.getTime() + 24 * 60 * 60 * 1000);
        
        // Formatear a String local ISO para la API (YYYY-MM-DDTHH:mm)
        const offset = hastaDate.getTimezoneOffset();
        const localHasta = new Date(hastaDate.getTime() - (offset * 60 * 1000));
        const hastaVal = localHasta.toISOString().slice(0, 16);

        try {
            const res = await apiRequest("/api/autorizaciones", "POST", {
                residenteId: parseInt(residenteId),
                visitanteNombre,
                visitanteDni,
                fechaDesde: desdeVal,
                fechaHasta: hastaVal
            });
            logSystem(`Autorización #${res.id} creada para ${visitanteNombre} (DNI: ${visitanteDni})`, "system");
            showToast(`Autorización para "${visitanteNombre}" generada con éxito por 24 hs.`, "success");
            document.getElementById("form-autorizar").reset();
            
            // Recargar datos y re-poblar los selectores del guardia (para que aparezca en vivo)
            await cargarDatosPorRol("RESIDENTE");
        } catch (err) {}
    });

    document.getElementById("form-checkin").addEventListener("submit", async (e) => {
        e.preventDefault();
        const seguridadId = document.getElementById("checkin-seguridad").value;
        const checkinPersonaId = document.getElementById("checkin-persona-select").value;
        const visitanteDni = document.getElementById("checkin-visitante-dni").value.trim();

        if (!checkinPersonaId) {
            showToast("Debe seleccionar una persona a ingresar de la lista.", "warning");
            return;
        }

        try {
            const res = await apiRequest("/api/visitas/ingreso", "POST", {
                visitanteDni,
                seguridadId: parseInt(seguridadId)
            });
            
            const persObj = personas.find(p => p.id == checkinPersonaId);
            const rolLabel = persObj ? persObj.tipo : "Visitante";
            
            logSystem(`Ingreso de Persona DNI ${visitanteDni} registrado por guardia ID #${seguridadId} (Visita #${res.id})`, "system");
            logSystem(`[Push Notification] Alerta de ingreso de ${rolLabel} al predio`, "push");
            showToast(`Ingreso registrado con éxito (Visita #${res.id}).`, "success");
            document.getElementById("form-checkin").reset();
            await cargarDatosPorRol("GUARDIA");
        } catch (err) {}
    });

    // --- HABILITACIÓN/DESHABILITACIÓN INTERACTIVA ---
    window.togglePersonaHabilitacion = async (id) => {
        try {
            await apiRequest(`/api/personas/${id}/toggle-habilitacion`, "PUT");
            showToast("Estado de persona modificado con éxito.", "success");
            const currentRol = document.getElementById("role-select").value;
            await cargarDatosPorRol(currentRol);
        } catch (err) {
            showToast("Error al modificar la habilitación de la persona.", "danger");
        }
    };

    window.toggleUnidadHabilitacion = async (id) => {
        try {
            await apiRequest(`/api/unidades/${id}/toggle-habilitacion`, "PUT");
            showToast("Estado de unidad funcional modificado con éxito.", "success");
            const currentRol = document.getElementById("role-select").value;
            await cargarDatosPorRol(currentRol);
        } catch (err) {
            showToast("Error al modificar la habilitación de la unidad.", "danger");
        }
    };

    // --- INICIALIZACIÓN GENERAL ---
    cargarDatosPorRol("ADMINISTRADOR");
});
