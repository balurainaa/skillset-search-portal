// 1. Register Employee
document.getElementById("registerForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const msgEl = document.getElementById("registerMsg");
    const body = {
        firstName: document.getElementById("reg_firstName").value,
        lastName: document.getElementById("reg_lastName").value,
        email: document.getElementById("reg_email").value,
        password: document.getElementById("reg_password").value,
        mobile: Number(document.getElementById("reg_mobile").value),
        dateOfJoining: document.getElementById("reg_dateOfJoining").value,
        yearOfExperience: Number(document.getElementById("reg_yearOfExperience").value),
        reportingTo: document.getElementById("reg_reportingTo").value ? Number(document.getElementById("reg_reportingTo").value) : null,
        roleName: document.getElementById("reg_roleName").value,
        skillName: document.getElementById("reg_skillName").value || null,
        proficiencyName: document.getElementById("reg_proficiencyName").value || null,
        skillExperience: document.getElementById("reg_skillExperience").value ? Number(document.getElementById("reg_skillExperience").value) : null,
        projects: csvToList(document.getElementById("reg_projects").value),
        certifications: csvToList(document.getElementById("reg_certifications").value)
    };
    postJson(`${BASE_URL}/api/profile/submit`, body, msgEl, (data) => {
        showMsg(msgEl, data, false);
        document.getElementById("registerForm").reset();
    }, false); // registration itself doesn't need a token (permitAll on the backend)
});

// 2. Assign Role
document.getElementById("roleForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const msgEl = document.getElementById("roleMsg");
    const employeeId = document.getElementById("role_employeeId").value;
    const roleName = document.getElementById("role_roleName").value;
    const url = `${BASE_URL}/api/profile/role?employeeId=${encodeURIComponent(employeeId)}&roleName=${encodeURIComponent(roleName)}`;
    fetch(url, { method: "POST", headers: getAuthHeader() ? { "Authorization": getAuthHeader() } : {} })
        .then(async (response) => {
            if (response.status === 401 || response.status === 403) {
                showMsg(msgEl, "Not authorized. Admins only.", true);
                return;
            }
            const text = await response.text();
            if (!response.ok) { showMsg(msgEl, text || "Something went wrong", true); return; }
            showMsg(msgEl, text, false);
            document.getElementById("roleForm").reset();
        })
        .catch(() => showMsg(msgEl, "Could not reach the server. Is it running on port 8080?", true));
});

// 3. Delete Profile
document.getElementById("deleteProfileForm").addEventListener("submit", async function (e) {
    e.preventDefault();
    const msgEl = document.getElementById("deleteProfileMsg");
    const employeeId = document.getElementById("del_employeeId").value;
    try {
        const headers = {};
        if (getAuthHeader()) headers["Authorization"] = getAuthHeader();
        const response = await fetch(`${BASE_URL}/api/profile/delete/${employeeId}`, { method: "DELETE", headers });
        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                showMsg(msgEl, "Not authorized. Admins only.", true);
                return;
            }
            showMsg(msgEl, "Something went wrong", true);
            return;
        }
        const text = await response.text();
        showMsg(msgEl, text, false);
        document.getElementById("deleteProfileForm").reset();
    } catch (err) {
        showMsg(msgEl, "Could not reach the server. Is it running on port 8080?", true);
    }
});
