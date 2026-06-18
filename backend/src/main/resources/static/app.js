document.addEventListener("DOMContentLoaded", () => {
    // --- STORES & DATA STATE ---
    let barrios = [];
    let unidades = [];
    let personas = [];
    let residentes = [];
    let guardias = [];
    let proveedores = [];
    let reclamos = [];
    let visitasActivas = [];

    // --- DOM ELEMENTS ---
    const roleSelect = document.getElementById("role-select");
    const panels = {
        ADMINISTRADOR: document.getElementById("panel-admin"),
        RESIDENTE: document.getElementById("panel-residente"),
        GUARDIA: document.getElementById("panel-guardia"),
        PROVEEDOR: document.getElementById("panel-proveedor")
    };

    // --- SYSTEM CONSOLE LOGGING ---
    const consoleLog = document.getElementById("notification-console");

    function logSystem(message, type = "system") {
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
                throw new Error(errText || `Error de servidor: ${response.status}`);
            }
            if (response.status === 204) return null;
            return await response.json();
        } catch (error) {
            logSystem(`Error API (${url}): ${error.message}`, "system");
            console.error(error);
            alert(`Error: ${error.message}`);
            throw error;
        }
    }

    // --- CARGA DE DATOS ---
    async function cargarDatosPorRol(rol) {
        await cargarBarrios();
        await cargarUnidades();
        
        if (rol === "ADMINISTRADOR") {
            await cargarPersonas();
            await cargarReclamosAdmin();
        } else if (rol === "RESIDENTE") {
            await cargarResidentesYVisitantes();
            await cargarReclamosResidente();
        } else if (rol === "GUARDIA") {
            await cargarDatosGuardia();
            await cargarVisitasActivas();
        } else if (rol === "PROVEEDOR") {
            await cargarDatosProveedor();
        }
    }

    // Cargar Barrios
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
        } catch (e) {
            console.log("Error cargando barrios");
        }
    }

    // Cargar Unidades
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
            renderTablaUnidades();
        } catch (e) {
            console.log("Error cargando unidades");
        }
    }

    // Cargar Personas (Tabla global)
    async function cargarPersonas() {
        try {
            const listRes = await apiRequest("/api/residentes");
            const listPers = await apiRequest("/api/personal");
            personas = [...listRes.map(r => ({ ...r, tipo: "RESIDENTE" })), ...listPers];
            renderTablaPersonas();
        } catch (e) {
            console.log("Error cargando personas");
        }
    }

    // --- RENDERIZADO DE TABLAS (ADMINISTRADOR) ---
    function renderTablaUnidades() {
        const tbody = document.querySelector("#tabla-unidades tbody");
        tbody.innerHTML = "";
        unidades.forEach(u => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${u.id}</td>
                <td>${u.identificador}</td>
                <td><span class="badge badge-muted">${u.tipoUnidad}</span></td>
            `;
            tbody.appendChild(tr);
        });
    }

    function renderTablaPersonas() {
        const tbody = document.querySelector("#tabla-personas tbody");
        tbody.innerHTML = "";
        personas.forEach(p => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>${p.nombre} ${p.apellido}</td>
                <td>${p.dni}</td>
                <td><span class="badge ${p.tipo === 'RESIDENTE' ? 'badge-success' : 'badge-process'}">${p.tipo}</span></td>
            `;
            tbody.appendChild(tr);
        });
    }

    // --- ACCIONES FORMULARIOS (Developer 1 Scope) ---

    // Crear Barrio
    document.getElementById("form-barrio").addEventListener("submit", async (e) => {
        e.preventDefault();
        const nombre = document.getElementById("barrio-nombre").value.trim();
        const direccion = document.getElementById("barrio-direccion").value.trim();
        
        try {
            const result = await apiRequest("/api/barrios", "POST", { nombre, direccion });
            logSystem(`Barrio creado: ${result.nombre} (ID: ${result.id})`, "system");
            document.getElementById("form-barrio").reset();
            cargarBarrios();
        } catch (error) {}
    });

    // Registrar Unidad Funcional
    document.getElementById("form-誠nidad")?.addEventListener("submit", async (e) => {
        // En index.html tiene id form-unidad, fix typo en listener
        // (En HTML está form-unidad)
    });
    // Se mapea correctamente con fallback al id real
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
                document.getElementById("form-unidad").reset();
                cargarUnidades();
            } catch (error) {}
        });
    }

    // Formulario Registrar Persona
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
            formPersona.reset();
            groupUnidad.style.display = "flex"; // default
            groupServicio.style.display = "none";
            cargarPersonas();
        } catch (error) {}
    });

    // --- PLACEHOLDERS Y LÓGICA MOCK PARA DEV 2 Y 3 ---
    // (Estas funciones se inicializan vacías para que el proyecto corra sin errores,
    // y los Desarrolladores 2 y 3 las completarán según sus tareas)

    async function cargarResidentesYVisitantes() {
        try {
            const selectRes = document.getElementById("reclamo-residente");
            const selectResAut = document.getElementById("aut-residente");
            
            // El visitante se escribe al autorizar; solo cargamos residentes para los combos
            const resList = await apiRequest("/api/residentes");
            [selectRes, selectResAut].forEach(sel => {
                sel.innerHTML = '<option value="" disabled selected>Seleccione un residente...</option>';
                resList.forEach(r => {
                    const opt = document.createElement("option");
                    opt.value = r.id;
                    opt.textContent = `${r.nombre} ${r.apellido}`;
                    sel.appendChild(opt);
                });
            });
        } catch (e) {}
    }

    async function cargarReclamosResidente() {
        // Dev 3 completará la lógica para cargar reclamos del residente seleccionado
        console.log("Cargar reclamos residente...");
    }

    async function cargarReclamosAdmin() {
        try {
            reclamos = await apiRequest("/api/reclamos");
            renderTablaReclamosAdmin();
        } catch (e) {}
    }

    function renderTablaReclamosAdmin() {
        const tbody = document.querySelector("#tabla-reclamos-admin tbody");
        tbody.innerHTML = "";
        
        reclamos.forEach(r => {
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
                <td>${r.residenteId}</td>
                <td><strong>${r.tipoReclamo}</strong><br><small>${r.descripcion}</small></td>
                <td><span class="badge">${r.prioridad}</span></td>
                <td><span class="badge ${badgeClass}">${r.estado}</span></td>
                <td>${r.responsableId ? 'ID #' + r.responsableId : '<span class="text-muted">Sin asignar</span>'}</td>
                <td>
                    ${r.estado === 'PENDIENTE' ? `<button class="btn btn-primary btn-sm btn-asignar" data-id="${r.id}" style="padding: 0.25rem 0.5rem; font-size: 0.75rem;">Asignar</button>` : ''}
                </td>
            `;
            tbody.appendChild(tr);
        });

        // Event listener para botón de asignar
        document.querySelectorAll(".btn-asignar").forEach(btn => {
            btn.addEventListener("click", async (e) => {
                const reclamoId = e.target.getAttribute("data-id");
                const responsableId = prompt("Ingrese el ID del Personal/Proveedor a asignar:");
                if (responsableId) {
                    try {
                        const res = await apiRequest(`/api/reclamos/${reclamoId}/asignar`, "PUT", {
                            responsableId: parseInt(responsableId)
                        });
                        logSystem(`Reclamo #${reclamoId} asignado al responsable ID #${responsableId} (Estado: EN_PROCESO)`, "system");
                        logSystem(`[Email enviado] Notificación enviada al residente del Reclamo #${reclamoId}`, "sms");
                        cargarReclamosAdmin();
                    } catch (err) {}
                }
            });
        });
    }

    // --- PLACEHOLDERS GUARDIA Y PROVEEDOR ---
    async function cargarDatosGuardia() {
        try {
            const selectGuardia = document.getElementById("checkin-seguridad");
            
            const persList = await apiRequest("/api/personal");
            const guardiasList = persList.filter(p => p.tipo === "SEGURIDAD");

            selectGuardia.innerHTML = '<option value="" disabled selected>Seleccione guardia...</option>';
            guardiasList.forEach(g => {
                const opt = document.createElement("option");
                opt.value = g.id;
                opt.textContent = `${g.nombre} ${g.apellido}`;
                selectGuardia.appendChild(opt);
            });
        } catch (e) {}
    }

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
        visitasActivas.forEach(v => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>#${v.id}</td>
                <td>${v.nombreVisitante || "Visitante"} - DNI: ${v.dniVisitante || "s/d"}</td>
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
                    logSystem(`Visita de Visitante ID #${visitanteId} registrada de salida (Portería)`, "system");
                    cargarVisitasActivas();
                } catch (err) {}
            });
        });
    }

    // Cargar combos para Proveedor
    async function cargarDatosProveedor() {
        try {
            const selectProv = document.getElementById("proveedor-filtro");
            const persList = await apiRequest("/api/personal");
            const operarios = persList.filter(p => p.tipo === "PROVEEDOR" || p.tipo === "MANTENIMIENTO");

            selectProv.innerHTML = '<option value="" disabled selected>Seleccione operario...</option>';
            operarios.forEach(op => {
                const opt = document.createElement("option");
                opt.value = op.id;
                opt.textContent = `${op.nombre} ${op.apellido} (${op.tipo})`;
                selectProv.appendChild(opt);
            });
        } catch (e) {}
    }

    // Filtrar tareas del Proveedor
    document.getElementById("proveedor-filtro").addEventListener("change", async (e) => {
        const id = e.target.value;
        try {
            const list = await apiRequest(`/api/reclamos?responsableId=${id}`);
            renderTablaTareasProveedor(list, id);
        } catch (err) {}
    });

    function renderTablaTareasProveedor(tareas, responsableId) {
        const tbody = document.querySelector("#tabla-tareas-proveedor tbody");
        tbody.innerHTML = "";
        
        tareas.forEach(t => {
            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td>#${t.id}</td>
                <td>${t.descripcion}</td>
                <td><span class="badge">${t.prioridad}</span></td>
                <td><span class="badge badge-process">${t.estado}</span></td>
                <td>
                    ${t.estado === 'EN_PROCESO' ? `<button class="btn btn-success btn-sm btn-resolver" data-id="${t.id}" style="padding: 0.25rem 0.5rem; font-size: 0.75rem;">Resolver</button>` : ''}
                    ${t.estado === 'PENDIENTE' ? `<span class="text-muted">Acepte la tarea en el panel de Admin</span>` : ''}
                    ${t.estado === 'RESUELTO' || t.estado === 'CERRADO' ? `<span class="badge badge-success">Completado</span>` : ''}
                </td>
            `;
            tbody.appendChild(tr);
        });

        document.querySelectorAll(".btn-resolver").forEach(btn => {
            btn.addEventListener("click", async (e) => {
                const id = e.target.getAttribute("data-id");
                const obs = prompt("Ingrese observaciones de resolución:");
                try {
                    await apiRequest(`/api/reclamos/${id}/estado`, "PUT", {
                        nuevoEstado: "RESUELTO",
                        observacion: obs || "Trabajo finalizado."
                    });
                    logSystem(`Reclamo #${id} resuelto por responsable ID #${responsableId}`, "system");
                    logSystem(`[Email enviado] Reclamo #${id} ha sido RESUELTO por el proveedor.`, "sms");
                    logSystem(`[Push Alert] Alerta de resolución enviada al residente del Reclamo #${id}`, "push");
                    
                    // Recargar tareas
                    const list = await apiRequest(`/api/reclamos?responsableId=${responsableId}`);
                    renderTablaTareasProveedor(list, responsableId);
                } catch (err) {}
            });
        });
    }

    // --- FORM SUBS FOR DEV 2 & 3 ---
    // Enlazar form-reclamo (Dev 3)
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
            logSystem(`[Push Alert] Nuevo reclamo #${res.id} registrado para Administración`, "push");
            document.getElementById("form-reclamo").reset();
            cargarReclamosResidente();
        } catch (err) {}
    });

    // Enlazar form-autorizar (Dev 2)
    document.getElementById("form-autorizar").addEventListener("submit", async (e) => {
        e.preventDefault();
        const residenteId = document.getElementById("aut-residente").value;
        const visitanteNombre = document.getElementById("aut-visitante-nombre").value.trim();
        const visitanteDni = document.getElementById("aut-visitante-dni").value.trim();
        const desdeVal = document.getElementById("aut-desde").value;
        const hastaVal = document.getElementById("aut-hasta").value;

        try {
            const res = await apiRequest("/api/autorizaciones", "POST", {
                residenteId: parseInt(residenteId),
                visitanteNombre,
                visitanteDni,
                fechaDesde: desdeVal,
                fechaHasta: hastaVal
            });
            logSystem(`Autorización #${res.id} creada para ${visitanteNombre} (DNI: ${visitanteDni})`, "system");
            document.getElementById("form-autorizar").reset();
        } catch (err) {}
    });

    // Enlazar form-checkin (Dev 2)
    document.getElementById("form-checkin").addEventListener("submit", async (e) => {
        e.preventDefault();
        const seguridadId = document.getElementById("checkin-seguridad").value;
        const visitanteDni = document.getElementById("checkin-visitante-dni").value.trim();

        try {
            const res = await apiRequest("/api/visitas/ingreso", "POST", {
                visitanteDni,
                seguridadId: parseInt(seguridadId)
            });
            logSystem(`Ingreso exitoso de visitante DNI ${visitanteDni} registrado por guardia ID #${seguridadId} (Visita #${res.id})`, "system");
            logSystem(`[Push Alert] Visitante ingresando a Unidad Funcional`, "push");
            document.getElementById("form-checkin").reset();
            cargarVisitasActivas();
        } catch (err) {}
    });

    // --- INICIALIZACIÓN ---
    cargarDatosPorRol("ADMINISTRADOR");
});
