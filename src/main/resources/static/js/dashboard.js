// 1. Add Skill
document.getElementById("skillForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const msgEl = document.getElementById("skillMsg");
    const body = {
        employeeId: Number(document.getElementById("skill_employeeId").value),
        skillName: document.getElementById("skill_skillName").value,
        proficiencyName: document.getElementById("skill_proficiencyName").value,
        yearsWorked: Number(document.getElementById("skill_yearsWorked").value)
    };
    postJson(`${BASE_URL}/api/skills`, body, msgEl, (data) => {
        showMsg(msgEl, `Added: ${data.employeeName} — ${data.skillName} (${data.proficiencyName})`, false);
        document.getElementById("skillForm").reset();
    });
});

// 2. Search by Skill
document.getElementById("searchForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const msgEl = document.getElementById("searchMsg");
    const table = document.getElementById("resultsTable");
    const tbody = document.getElementById("resultsBody");
    tbody.innerHTML = "";
    table.style.display = "none";
    msgEl.textContent = "";
    const skillName = document.getElementById("search_skillName").value;
    getJson(`${BASE_URL}/api/skills/search?skillName=${encodeURIComponent(skillName)}`, msgEl, (data) => {
        if (data.length === 0) {
            showMsg(msgEl, "No employees found with that skill.", false);
            return;
        }
        data.forEach(row => {
            const tr = document.createElement("tr");
            tr.innerHTML = `<td>${row.employeeName}</td><td>${row.skillName}</td><td>${row.proficiencyName}</td><td>${row.yearsWorked}</td>`;
            tbody.appendChild(tr);
        });
        table.style.display = "table";
    });
});

// 3. Add Project
document.getElementById("projectForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const msgEl = document.getElementById("projectMsg");
    const body = {
        employeeId: Number(document.getElementById("project_employeeId").value),
        projectName: document.getElementById("project_projectName").value
    };
    postJson(`${BASE_URL}/api/profile/project`, body, msgEl, (data) => {
        showMsg(msgEl, data, false);
        document.getElementById("projectForm").reset();
    });
});

// 4. Add Certification
document.getElementById("certificationForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const msgEl = document.getElementById("certMsg");
    const body = {
        employeeId: Number(document.getElementById("cert_employeeId").value),
        certificationName: document.getElementById("cert_certificationName").value
    };
    postJson(`${BASE_URL}/api/profile/certification`, body, msgEl, (data) => {
        showMsg(msgEl, data, false);
        document.getElementById("certificationForm").reset();
    });
});

// 5. View Profile
document.getElementById("profileForm").addEventListener("submit", function (e) {
    e.preventDefault();
    const msgEl = document.getElementById("profileMsg");
    const profileCard = document.getElementById("profileCard");
    profileCard.style.display = "none";
    msgEl.textContent = "";
    const employeeId = document.getElementById("profile_employeeId").value;
    getJson(`${BASE_URL}/api/profile/${employeeId}`, msgEl, (data) => {
        document.getElementById("profileName").textContent = `${data.firstName} ${data.lastName} (ID: ${data.employeeId})`;
        document.getElementById("profileEmail").textContent = data.email;
        document.getElementById("profileMobile").textContent = data.mobile;
        document.getElementById("profileRole").textContent = data.roleName;

        const skillsBody = document.getElementById("profileSkillsBody");
        skillsBody.innerHTML = "";
        (data.skills || []).forEach(s => {
            const tr = document.createElement("tr");
            tr.innerHTML = `<td>${s.skillName}</td><td>${s.proficiencyName}</td><td>${s.yearsWorked}</td>`;
            skillsBody.appendChild(tr);
        });

        const projectsList = document.getElementById("profileProjectsList");
        projectsList.innerHTML = "";
        (data.projects || []).forEach(p => {
            const li = document.createElement("li");
            li.className = "list-group-item";
            li.textContent = p;
            projectsList.appendChild(li);
        });

        const certsList = document.getElementById("profileCertsList");
        certsList.innerHTML = "";
        (data.certifications || []).forEach(c => {
            const li = document.createElement("li");
            li.className = "list-group-item";
            li.textContent = c;
            certsList.appendChild(li);
        });

        profileCard.style.display = "block";
    });
});

// 6. Update Profile
document.getElementById("updateProfileForm").addEventListener("submit", async function (e) {
    e.preventDefault();
    const msgEl = document.getElementById("updateProfileMsg");
    const employeeId = document.getElementById("upd_employeeId").value;
    const body = {
        firstName: document.getElementById("upd_firstName").value,
        lastName: document.getElementById("upd_lastName").value,
        email: document.getElementById("upd_email").value,
        password: document.getElementById("upd_password").value,
        mobile: Number(document.getElementById("upd_mobile").value),
        dateOfJoining: document.getElementById("upd_dateOfJoining").value,
        yearOfExperience: Number(document.getElementById("upd_yearOfExperience").value),
        reportingTo: document.getElementById("upd_reportingTo").value ? Number(document.getElementById("upd_reportingTo").value) : null,
        roleName: document.getElementById("upd_roleName").value
    };
    try {
        const headers = { "Content-Type": "application/json" };
        if (getAuthHeader()) headers["Authorization"] = getAuthHeader();
        const response = await fetch(`${BASE_URL}/api/profile/update/${employeeId}`, {
            method: "PUT", headers, body: JSON.stringify(body)
        });
        if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
                showMsg(msgEl, "Not authorized to update this profile.", true);
                return;
            }
            const errorData = await response.json().catch(() => null);
            const firstError = errorData && typeof errorData === "object" ? Object.values(errorData)[0] : null;
            showMsg(msgEl, firstError || "Something went wrong", true);
            return;
        }
        const text = await response.text();
        showMsg(msgEl, text, false);
    } catch (err) {
        showMsg(msgEl, "Could not reach the server. Is it running on port 8080?", true);
    }
});
