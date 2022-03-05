package ru.test.alex.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.test.alex.entity.Worker;
import ru.test.alex.service.WorkerService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    private String name = "";
    private String token = "";

    private final WorkerService workerService;

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        log.info(text);
        if (text.equalsIgnoreCase("/Работники")) {
            List<Worker> workers = workerService.getAll();
            send(new SendMessage(update.getMessage().getChatId().toString(), parseWorkersToMessage(workers)));
            return;
        }

        String[] words = text.split(" ");
        int cash = parseCash(words[1]);
        if (words.length > 2 || cash < 1) {
            send(new SendMessage(update.getMessage().getChatId().toString(),
                    "Нераспознанная команда, для зачисления используй формат: <Имя> <Сумма>"));
            return;
        }
        Worker updated = workerService.updateCash(words[0], cash);

        send(new SendMessage(update.getMessage().getChatId().toString(),
                updated != null ? "Средства зачислены!\n" + updated.getName() + " " + updated.getCash()
                        : "Работник не найден"));
    }

    private void send(BotApiMethod<Message> method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private int parseCash(String strCash) {
        try {
            return Integer.parseInt(strCash);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String parseWorkersToMessage(List<Worker> workers) {
        StringBuilder builder = new StringBuilder();
        return IntStream.range(0, workers.size()).mapToObj(value -> {
            Worker worker = workers.get(value);
            return value + ": " + worker.getName() + " " + worker.getCash() + "\n";
        }).collect(Collectors.joining());
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
