import 'package:flutter/material.dart';
import '../models/word.dart';
import '../services/api_service.dart';
import 'word_detail_screen.dart';
import 'word_form_screen.dart';

class WordListScreen extends StatefulWidget {
  const WordListScreen({super.key});
  @override
  _WordListScreenState createState() => _WordListScreenState();
}

class _WordListScreenState extends State<WordListScreen> {
  final api = ApiService();
  late Future<List<Word>> words;

  @override
  void initState() {
    super.initState();
    words = api.fetchWords();
  }

  void refresh() => setState(() => words = api.fetchWords());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Words'),
        // Home button (although we’re already on home, it's here for consistency)
        actions: [
          IconButton(
            icon: const Icon(Icons.home),
            onPressed: () {
              Navigator.pushAndRemoveUntil(
                context,
                MaterialPageRoute(builder: (_) => const WordListScreen()),
                (route) => false,
              );
            },
          )
        ],
      ),
      body: FutureBuilder<List<Word>>(
        future: words,
        builder: (ctx, snap) {
          if (snap.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snap.hasError) {
            return Center(child: Text('Error: ${snap.error}'));
          }
          final list = snap.data!;
          return ListView.builder(
            itemCount: list.length,
            itemBuilder: (ctx, i) {
              final w = list[i];
              return ListTile(
                title: Text(w.name),
                subtitle: Text(w.definition),
                onTap: () => Navigator.push(
                  context,
                  MaterialPageRoute(builder: (_) => WordDetailScreen(word: w)),
                ).then((_) => refresh()),
                trailing: Row(
                  mainAxisSize: MainAxisSize.min,
                  children: [
                    IconButton(
                      icon: const Icon(Icons.edit),
                      onPressed: () => Navigator.push(
                        context,
                        MaterialPageRoute(builder: (_) => WordFormScreen(word: w)),
                      ).then((_) => refresh()),
                    ),
                    IconButton(
                      icon: const Icon(Icons.delete),
                      onPressed: () => api.deleteWord(w.id).then((_) => refresh()),
                    ),
                  ],
                ),
              );
            },
          );
        },
      ),
      // “New Word” button
      floatingActionButton: FloatingActionButton(
        child: const Icon(Icons.add),
        onPressed: () =>
            Navigator.push(context, MaterialPageRoute(builder: (_) => const WordFormScreen()))
                .then((_) => refresh()),
      ),
    );
  }
}