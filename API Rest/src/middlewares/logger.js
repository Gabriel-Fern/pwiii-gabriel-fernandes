
function logger(req, res, next) {
    // Marca o tempo do início da requisição
    const start = Date.now();
    const timestamp = new Date().toISOString(); 
    //Evento Finish só vai ser disparado quando a resposta for enviada
    res.on("finish", () => {
        const duration = Date.now() - start;
        // Exibe o log
        console.log(`[${timestamp}] ${req.method} ${req.originalUrl} — ${res.statusCode} (${duration}ms)`);
    });

    next();
}

module.exports = logger;
