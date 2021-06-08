package com.company.graphics;

import com.company.enums.Languages;

public class Language {
    Languages currentLang;
    int language_code = 0;

    //exit window
    String[] confirmExit = {"Are you sure you want to leave the database?","Вы уверены, что хотите покинуть БД?","Вы уверены, что хотите покинуть БД?", "Ar tikrai norite išeiti iš duomenų bazės?"};
    String[] yes = {"Yes", "Да", "Да","Taip"};
    String[] no = {"No", "Нет", "Нет", "Ne"};

    //messages
    String[] invalidName = {"Invalid name", "Некорректное имя", "Некорректное имя", "Netinkamas vardas"};
    String[] invalidSalary = {"Invalid salary", "Некорректное зп", "Некорректное зп", "Netinkamas atlyginimas"};
    String[] invalidPersonality = {"Invalid personality", "Некорректное личность", "Некорректное личность", "Netinkamas asmenybė"};
    String[] invalidCoordinates = {"Invalid coordinates", "Некорректное координаты", "Некорректное координаты", "Netinkamas koordinatės"};
    String[] invalidStart = {"Invalid start date", "Неверная дата начала", "Неверная дата начала", "Netinkamas pradžios data"};
    String[] invalidEnd = {"Invalid end date", "Неверная дата конца", "Неверная дата конца", "Netinkamas pabaigos data"};

    String[] invalidLogin = {"Login can't be empty", "Логин не должен быть пустым", "Логин не должен быть пустым", "Prisijungimas negali būti tuščias"};
    String[] invalidPassword =  {"Password can't be empty", "Пароль не должен быть пустым", "Пароль не должен быть пустым", "Slaptažodis negali būti tuščias"};
    String[] passNotMatch = {"Passwords don't match", "Пароли не совпадают", "Пароли не совпадают", "Slaptažodžiai nesutampa"};
    String[] noUser = {"This user doen't exist", "Такого пользователя не существует", "Такого пользователя не существует", "Šio vartotojo nėra"};

    //login panel
    String[] loginLabel = {"Enter login: ", "Введите логин: ", "Введите логин: ", "Iveskite prisijungimo vardą: "};
    String[] passLabel = {"Enter password: ", "Введите пароль: ", "Введите пароль: ", "Ivesti slaptažodį: "};
    String[] repeatPassLabel = {"Repeat password: ", "Повторите пароль", "Повторите пароль","Pakartokite slaptažodį"};
    String[] loginButton = {"Log in", "Войти", "Войти", "Prisijungti"};
    String[] registerButton = {"Register", "Зарегистрироваться", "Зарегистрироваться", "Registruokis"};

    String[] registerInsteadButton = {"Register instead", "Зарегистрироваться", "Зарегистрироваться", "Registruokis"};
    String[] loginInsteadButton = {"Log in instead", "Войти", "Войти", "Prisijungti"};

    //database panel
    String[] addButton = {"Add worker", "Добавить работника", "Добавить работника", "Pridėti darbuotoją"};
    String[] removeButton = {"Remove seleccted", "Удалить выбранного", "Удалить выбранного", "Pašalinti pasirinktus"};
    String[] removeGreaterButton = {"Remove greater", "Удалить больших",  "Удалить больших", "Pašalinti didesnį"};
    String[] removeLowerButton = {"Remove lower", "Удалить меньших", "Удалить меньших", "Nuimkite apatinę"};
    String[] updateButton = {"Update selected", "Изменить", "Изменить", "Pasirinktas atnaujinimas"};
    String[] clearButton = {"Clear database", "Очистить БД", "Очистить БД", "Išvalyti duomenų bazę"};
    String[] visButton = {"Visualisation", "Визуализация","Визуализация", "Vizualizacija"};
    String[] refreshButton = {"Refresh", "Обновить","Обновить","Atnaujinti"};
    String[] currentUser = {"Current user: ", "Текущий пользователь", "Текущий пользователь", "Dabartinis vartotojas"};
    String[] countPositionsButton = {"Count position", "Посчитать должности", "Посчитать должности", "Skaičiuokite įrašus"};

    //add worker panel
    String[] nameLabel = {"Name: ", "Имя: ","Имя: ","Vardas"};
    String[] salaryLabel = {"Salary: ", "Зарплата: ", "Зарплата: ", "Atlyginimas: "};
    String[] positionLabel = {"Position: ", "Должность: ", "Должность: ", "Poziciją: "};
    String[] personLabel = {"Personality: ", "Личность: ", "Личность: ", "Asmenybė: "};
    String[] coordLabel = {"Coordinates: ", "Координаты: ", "Координаты: ", "Koordinatės: "};
    String[] startDateLabel = {"Start date: ", "Первый день: ", "Первый день: ", "Pradžios data: "};
    String[] endDateLabel = {"End date: ", "Последний день: ", "Последний день: ", "Pabaigos data: "};
    String[] addIfMaxLabel = {"Add if salary is max", "Добавить, если зп максимальная", "Добавить, если зп максимальная", "Pridėkite, jei atlyginimas yra maksimalus"};
    String[] submitButton = {"Submit", "Отправить", "Отправить", "Pateikti"};
    String[] backButton = {"Back", "Назад", "Назад", "Atgal"};

    //table
    String[] name = {"Name", "Имя", "Имя", "Vardas"};
    String[] id = {"ID", "Айди", "Айди", "ID"};
    String[] salary = {"Salary", "Зарплата", "Зарплата", "Atlyginimas"};
    String[] position = {"Position", "Должность", "Должность", "Poziciją"};
    String[] personality = {"Personality", "Личность", "Личность", "Asmenybė"};
    String[] coordinates = {"Coordinates", "Координаты", "Координаты", "Koordinatės"};
    String[] startDate = {"Start date", "Первый день", "Первый день", "Pradžios data"};
    String[] endDate = {"End date", "Последний день", "Последний день", "Pabaigos data"};
    String[] user = {"User", "Пользователь", "Пользователь", "Vartotojas"};

    public Languages getCurrentLang() {
        return currentLang;
    }

    public void setLanguage(Languages language){
        currentLang = language;
        switch (language){
            case en_NZ:
                language_code = 0;
                break;
            case ru_RU:
                language_code = 1;
                break;
            case bel_BEL:
                language_code = 2;
                break;
            case lit_LIT:
                language_code = 3;
                break;
        }
    }

    public String getLoginLabel() {
        return loginLabel[language_code];
    }

    public String getPassLabel() {
        return passLabel[language_code];
    }

    public String getRepeatPassLabel() {
        return repeatPassLabel[language_code];
    }

    public String getLoginButton() {
        return loginButton[language_code];
    }

    public String getRegisterButton() {
        return registerButton[language_code];
    }

    public String getAddButton() {
        return addButton[language_code];
    }

    public String getRemoveButton() {
        return removeButton[language_code];
    }

    public String getRemoveGreaterButton() {
        return removeGreaterButton[language_code];
    }

    public String getRemoveLowerButton() {
        return removeLowerButton[language_code];
    }

    public String getUpdateButton() {
        return updateButton[language_code];
    }

    public String getClearButton() {
        return clearButton[language_code];
    }

    public String getVisButton() {
        return visButton[language_code];
    }

    public String getRefreshButton() {
        return refreshButton[language_code];
    }

    public String getNameLabel() {
        return nameLabel[language_code];
    }

    public String getSalaryLabel() {
        return salaryLabel[language_code];
    }

    public String getPositionLabel() {
        return positionLabel[language_code];
    }

    public String getPersonLabel() {
        return personLabel[language_code];
    }

    public String getCoordLabel() {
        return coordLabel[language_code];
    }

    public String getStartDateLabel() {
        return startDateLabel[language_code];
    }

    public String getEndDateLabel() {
        return endDateLabel[language_code];
    }

    public String getAddIfMaxLabel() {
        return addIfMaxLabel[language_code];
    }

    public String getSubmitButton() {
        return submitButton[language_code];
    }

    public String getBackButton() {
        return backButton[language_code];
    }

    public String getInvalidName() {
        return invalidName[language_code];
    }

    public String getInvalidSalary() {
        return invalidSalary[language_code];
    }

    public String getInvalidPersonality() {
        return invalidPersonality[language_code];
    }

    public String getInvalidCoordinates() {
        return invalidCoordinates[language_code];
    }

    public String getInvalidStart() {
        return invalidStart[language_code];
    }

    public String getInvalidEnd() {
        return invalidEnd[language_code];
    }

    public String getRegisterInsteadButton() {
        return registerInsteadButton[language_code];
    }

    public String getLoginInsteadButton() {
        return loginInsteadButton[language_code];
    }

    public String getInvalidLogin() {
        return invalidLogin[language_code];
    }

    public String getInvalidPassword() {
        return invalidPassword[language_code];
    }

    public String getPassNotMatch() {
        return passNotMatch[language_code];
    }

    public String getNoUser() {
        return noUser[language_code];
    }

    public String getConfirmExit() {
        return confirmExit[language_code];
    }

    public String getYes() {
        return yes[language_code];
    }

    public String getNo() {
        return no[language_code];
    }

    public String getCurrentUser() {
        return currentUser[language_code];
    }

    public String getName() {
        return name[language_code];
    }

    public String getId() {
        return id[language_code];
    }

    public String getSalary() {
        return salary[language_code];
    }

    public String getPosition() {
        return position[language_code];
    }

    public String getPersonality() {
        return personality[language_code];
    }

    public String getCoordinates() {
        return coordinates[language_code];
    }

    public String getStartDate() {
        return startDate[language_code];
    }

    public String getEndDate() {
        return endDate[language_code];
    }

    public String getUser() {
        return user[language_code];
    }

    public String getCountPositionsButton() {
        return countPositionsButton[language_code];
    }
}
